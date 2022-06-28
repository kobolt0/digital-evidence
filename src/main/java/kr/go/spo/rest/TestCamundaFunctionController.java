package kr.go.spo.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.context.BpmnExecutionContext;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestCamundaFunctionController {
  private SqlSessionTemplate sqlSessionTemplate;

  // 프로세스 시작
  @GetMapping("/startPorc")
  public String startPorc(HttpServletRequest req) {
    log.debug("##@# REST. {}", req.getRequestURL());
    Map<String, Object> variables=new HashMap<>();
    variables.put("caseId","nombrePdf");
    variables.put("datos","datos");

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    RuntimeService runtimeService=processEngine.getRuntimeService();

    

    ProcessInstanceWithVariables instance = runtimeService.createProcessInstanceByKey("preprocess")
            .setVariables(variables)
            .executeWithVariablesInReturn();
    return null;
  }

  // 진행중인 프로세스 접근

  @GetMapping("/runningProc")
  public String runningProc(HttpServletRequest req) {
    log.debug("##@# REST. {}", req.getRequestURL());
    BpmnExecutionContext con = Context.getBpmnExecutionContext();
    ExecutionEntity inst = Context.getBpmnExecutionContext().getProcessInstance();
    return null;
  }

}