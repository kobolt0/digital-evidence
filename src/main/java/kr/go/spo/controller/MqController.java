package kr.go.spo.controller;

import kr.go.spo.dto.TrgtCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mq")
public class MqController {

  private final JmsTemplate jmsTemplate;


  @GetMapping("/produceMsg")
  public String produceMsg(HttpServletRequest req
          , @RequestParam(value="caseId", defaultValue="") String caseId
          , @RequestParam(value="priority", defaultValue="") String priority
  ) {

    jmsTemplate.setPubSubDomain(false);
    jmsTemplate.convertAndSend("trgtCase", new TrgtCase(caseId, priority));

    return null;
  }


}