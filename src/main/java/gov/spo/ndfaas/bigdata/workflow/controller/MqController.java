package gov.spo.ndfaas.bigdata.workflow.controller;

import gov.spo.ndfaas.bigdata.workflow.dto.TrgtCaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mq")
public class MqController {

  private final ApplicationContext applicationContext;
//  private final Connection connection;
  private final JmsTemplate jmsTemplate;
  private final ConnectionFactory connectionFactory;


  @GetMapping("/produceMsg")
  public String produceMsg(HttpServletRequest req
          , @RequestParam(value="processId", defaultValue="") String processId
          , @RequestParam(value="caseId", defaultValue="") String caseId
          , @RequestParam(value="priority", defaultValue="") String priority
  ) {
    TrgtCaseDto trgtCaseDto = new TrgtCaseDto(processId, caseId, priority);
    jmsTemplate.setPubSubDomain(false);
    jmsTemplate.convertAndSend("trgtCase", trgtCaseDto);

    log.debug("##@# JMS produceMsg [{}]", trgtCaseDto);

    return null;
  }

  @GetMapping("/mqTest")
  public List<TextMessage> mqTest() throws JMSException {
    //      ActiveMQConnection con = connectionFactory.createConnection();
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();

    ActiveMQConnection c = (ActiveMQConnection) activeMQConnectionFactory.createConnection();
    Set<ActiveMQQueue> qs = c.getDestinationSource().getQueues();
    return null;
  }




  @GetMapping("/jmsTest")
  public List<TextMessage> jmsTest() throws JMSException {


    // 컨텍스트에서 빈 이름 조회
//    if (applicationContext != null) {
//      String[] beans = applicationContext.getBeanDefinitionNames();
//
//      for (String bean : beans) {
//        System.out.println("bean : " + bean);
//      }
//    }

    Connection connection = null;
    try {

      // Producer
      connection = connectionFactory.createConnection();
      Session session = connection.createSession(false,
              Session.AUTO_ACKNOWLEDGE);
      Queue queue = session.createQueue("test1");


      MessageConsumer consumer = session.createConsumer(queue);

      connection.start();
      System.out.println("Browse through the elements in queue");
      QueueBrowser browser = session.createBrowser(queue);

      Enumeration<TextMessage> e = browser.getEnumeration();
      List <TextMessage> list = new ArrayList<>();


      //Multithreading here
      while (e.hasMoreElements()) {
        TextMessage message =  e.nextElement();
        list.add(message);
        System.out.println("Browse [" + message.getText() + "]");
      }
      System.out.println("Done");
      String rtn = e.toString();
      System.out.println(rtn);
      browser.close();

      session.close();
      return list;
    } finally {
      if (connection != null) {
        connection.close();
      }
    }

  }
}