package kr.go.spo.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/startPorc")
  public String testJsonObj() {



    Map<String, Object> variables=new HashMap<>();
    variables.put("nombrePdf","nombrePdf");
    variables.put("datos","datos");

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    RuntimeService runtimeService=processEngine.getRuntimeService();

    ProcessInstanceWithVariables instance = runtimeService.createProcessInstanceByKey("POCFirma")
            .setVariables(variables)
            .executeWithVariablesInReturn();
    return null;
  }

}