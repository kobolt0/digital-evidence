package gov.spo.ndfaas.bigdata.workflow.controller;

import gov.spo.ndfaas.bigdata.workflow.dto.TaskRunDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestRest {
  private final SqlSessionTemplate sqlSessionTemplate;

  private final JmsTemplate jmsTemplate;
  @GetMapping("/getMsg")
  public String getMsg() throws JMSException {
    Message msg = jmsTemplate.receive("trgtCase1");

    log.debug("msg.getJMSDestination():{}",msg.getJMSDestination());
    log.debug("msg.getJMSMessageID():{}",msg.getJMSMessageID());
    log.debug("msg.getJMSType():{}",msg.getJMSType());



    return "";
  }
  @GetMapping("/testJson")
  public String testJsonObj() {
    Map<String, String> map = sqlSessionTemplate.selectOne("test.selectTest");
    log.debug("##@# sql resutl:{}" , map);
    return map.toString();
  }
  @GetMapping("/test2")
  public String testJsonObj2() {

    TaskRunDto obj = sqlSessionTemplate.selectOne("test.selectTest2");
    log.debug("##@# sql resutl:{}" , obj);
    return obj.toString();
  }

  @GetMapping("/testList")
  public String testJsonList() {
//    Map<String, String> map = sqlSessionTemplate.selectOne("test.selectTest");
//    log.debug("##@# sql resutl:{}" , map);

    List<Map<String, String>> list = sqlSessionTemplate.selectList("test.selectTest");
    log.debug("##@# sql list resutl:{}" , list);

    return list.toString();
  }
  @GetMapping("/dummy")
  public Map<String, String> testDummy(HttpServletRequest req) throws InterruptedException {
    log.debug("##@# REST. {}", req.getRequestURL());
    Map<String, String> map = new HashMap<>();

    List <Map<String, String>> list = sqlSessionTemplate.selectList("test.dummy");
    log.debug("#@## sql result:{}", list);
    for (Map<String, String> tmpMap: list) {
      map.put(tmpMap.get("NAME"), tmpMap.get("VAL"));
    }

    //예외발생
    if ("Y".equals(map.get("exceptionYn"))){
      new Exception("##@# 예외발생");
    }


    int sleepTime = 1000 * Integer.valueOf(map.get("sleepTime"));
    Thread.sleep(sleepTime);
    log.debug("##@# sleep {} sec",sleepTime);
    return map;
  }


}