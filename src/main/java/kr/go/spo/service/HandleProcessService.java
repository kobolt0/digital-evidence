package kr.go.spo.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.context.BpmnExecutionContext;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@Service
public class HandleProcessService {

  private RuntimeService runtimeService;

  private RuntimeService getRuntimeService(){
    if (this.runtimeService == null){
      this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
    }
    return this.runtimeService;
  }

  // 프로세스 삭제

  // 프로세스 정지
  public String suspendAllProcess() {

    List<ProcessInstance> list = this.getRuntimeService().createProcessInstanceQuery().list();
    for (ProcessInstance pi: list
    ) {
      this.getRuntimeService().suspendProcessInstanceById(pi.getId());
    }

    return null;
  }
  // 프로세스 정지
  public String suspendProcess(String processInstanceId) {
    this.getRuntimeService().suspendProcessInstanceById(processInstanceId);
    return null;
  }

 // 프로세스 재시작
  public String resumeAllProcess() {

    List<ProcessInstance> list = this.getRuntimeService().createProcessInstanceQuery().suspended().list();
    for (ProcessInstance pi: list
    ) {
      this.getRuntimeService().activateProcessInstanceById(pi.getId());
    }

    return null;
  }

  // 프로세스 재시작
  public String resumeProcess(String processInstanceId) {
    this.getRuntimeService().activateProcessInstanceById(processInstanceId);
    return null;
  }

  // 프로세스 시작
  public ProcessInstance startProcess(String caseId, String prioty) {
    Map<String, Object> variables=new HashMap<>();
    variables.put("caseId",caseId);
    variables.put("prioty",prioty);

    ProcessInstanceWithVariables instance = this.getRuntimeService().createProcessInstanceByKey("preprocess")
            .setVariables(variables)
            .businessKey(caseId)
            .executeWithVariablesInReturn()
            ;

    return instance;
  }


}
