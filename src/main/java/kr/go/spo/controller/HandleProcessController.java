package kr.go.spo.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.context.BpmnExecutionContext;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.*;
import org.camunda.bpm.engine.task.Task;
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
@RestController
@RequestMapping("/test")
public class HandleProcessController {

  private RuntimeService runtimeService;

  private RuntimeService getRuntimeService(){
    if (this.runtimeService == null){
      this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
    }
    return this.runtimeService;
  }

    // 프로세스 삭제
    @GetMapping("/deleteAllProcess")
    public String deleteAllProcess(HttpServletRequest req, @RequestParam(value="deleteReason", defaultValue="") String deleteReason) {

        List<ProcessInstance> list = this.getRuntimeService().createProcessInstanceQuery().list();
        for (ProcessInstance pi: list) {

            // ACT_HI_PROCINST 테이블에 정보 insert
            this.getRuntimeService().deleteProcessInstance(pi.getId(), deleteReason);
        }

        return null;
    }

    //프로세스 삭제
    @GetMapping("/deleteProcess")
    public String deleteProcess(HttpServletRequest req, @RequestParam(value="processInstanceId", defaultValue="") String processInstanceId
            , @RequestParam(value="deleteReason", defaultValue="") String deleteReason) {
        // ACT_HI_PROCINST 테이블에 정보 insert
        this.getRuntimeService().deleteProcessInstance(processInstanceId,deleteReason);
        return null;
    }

  // 프로세스 정지
  @GetMapping("/suspendAllProcess")
  public String suspendAllProcess(HttpServletRequest req) {

    List<ProcessInstance> list = this.getRuntimeService().createProcessInstanceQuery().list();
    for (ProcessInstance pi: list
    ) {
      this.getRuntimeService().suspendProcessInstanceById(pi.getId());
    }

    return null;
  }
  // 프로세스 정지
  @GetMapping("/suspendProcess")
  public String suspendProcess(HttpServletRequest req, @RequestParam(value="processInstanceId", defaultValue="") String processInstanceId) {
    this.getRuntimeService().suspendProcessInstanceById(processInstanceId);
    return null;
  }

 // 프로세스 재시작
  @GetMapping("/resumeAllProcess")
  public String resumeAllProcess(HttpServletRequest req) {

    List<ProcessInstance> list = this.getRuntimeService().createProcessInstanceQuery().suspended().list();
    for (ProcessInstance pi: list
    ) {
      this.getRuntimeService().activateProcessInstanceById(pi.getId());
    }

    return null;
  }

  // 프로세스 재시작
  @GetMapping("/resumeProcess")
  public String resumeProcess(HttpServletRequest req, @RequestParam(value="processInstanceId", defaultValue="") String processInstanceId) {
    this.getRuntimeService().activateProcessInstanceById(processInstanceId);
    return null;
  }


    // 프로세스 시작
  @GetMapping("/startPorc")
  public String startPorc(HttpServletRequest req) {
    log.debug("##@# TEST. {}", req.getRequestURL());
    Map<String, Object> variables=new HashMap<>();
    variables.put("caseId","nombrePdf");
    variables.put("datos","datos");

    ProcessInstanceWithVariables instance = this.getRuntimeService().createProcessInstanceByKey("preprocess")
            .setVariables(variables)
            .executeWithVariablesInReturn();

    return instance.getId();
  }

  // 진행중인 프로세스 접근

  @GetMapping("/runningProc")
  public String runningProc(HttpServletRequest req, @RequestParam(value="val1", defaultValue="") String val1) {
    log.debug("##@# TEST. {}", req.getRequestURL());
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//    List<ProcessInstance> obj = getAllRunningProcessInstances(val1);
    ProcessInstanceQuery qry = this.getRuntimeService().createProcessInstanceQuery();
    List<ProcessInstance> list = qry.list();
//    ActivityInstance ai = runtimeService.getActivityInstance("Activity_virus:b764ed0e-f77d-11ec-8ad8-2acdc420f0a3");
//    String getId = ai.getId();
//    String getActivityId = ai.getActivityId();
    for (ProcessInstance pi: list
         ) {
        log.debug("##@# ProcessInstance");
        log.debug("##@# [{}]getId", pi.getId());
        log.debug("##@# [{}]getProcessInstanceId", pi.getProcessInstanceId());
        log.debug("##@# [{}]getBusinessKey", pi.getBusinessKey());
        log.debug("##@# [{}]getCaseInstanceId", pi.getCaseInstanceId());
        log.debug("##@# [{}]getCaseInstanceId", pi.getCaseInstanceId());
//      runtimeService.suspendProcessInstanceById(pi.getId());
        log.debug("##@# ProcessInstance END ###########");
    }



      List<VariableInstance> list1 = this.getRuntimeService().createVariableInstanceQuery()
              .processInstanceIdIn()
              .list();


      List<Execution> listex = this.getRuntimeService().createExecutionQuery()
              .activityId("Activity_virus")
              .list();
      ///////////////////
      TaskService taskService = processEngine.getTaskService();
      for (Execution ex :
              listex) {
          log.debug("##@# Execution");
          log.debug("##@# [{}]getId", ex.getId());
          log.debug("##@# [{}]getProcessInstanceId", ex.getProcessInstanceId());



          Map<String, Object> map = this.getRuntimeService().getVariables(ex.getId());
          log.debug("##@# [{}]map", map);

          List<Task> taskList = taskService.createTaskQuery().processInstanceId(ex.getProcessInstanceId()).list();

          log.debug("##@# Execution END");


      }



    List<Task> listTask = taskService.createTaskQuery().list();

    IdentityService identityService = processEngine.getIdentityService();
    FormService formService = processEngine.getFormService();
    HistoryService historyService = processEngine.getHistoryService();
    ManagementService managementService = processEngine.getManagementService();

    List<Job> obj = managementService.createJobQuery().executionId(listex.get(0).getId()).list();
      for (Job job :
           obj) {
          log.debug(job.getId());
          log.debug("");
      }

    FilterService filterService = processEngine.getFilterService();
    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
    CaseService caseService = processEngine.getCaseService();
    DecisionService decisionService = processEngine.getDecisionService();




//    BpmnExecutionContext con = Context.getBpmnExecutionContext();
//    ExecutionEntity inst = Context.getBpmnExecutionContext().getProcessInstance();
    BpmnExecutionContext obj1 = Context.getBpmnExecutionContext();



    return null;
  }


  public List<ProcessInstance> getAllRunningProcessInstances(String processDefinitionName) {
    // get process engine and services
    ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
    RepositoryService repositoryService = processEngine.getRepositoryService();

    // query for latest process definition with given name
    ProcessDefinition myProcessDefinition =
            repositoryService.createProcessDefinitionQuery()
                    .processDefinitionName(processDefinitionName)
                    .latestVersion()
                    .singleResult();

    // list all running/unsuspended instances of the process
    List<ProcessInstance> processInstances =
            runtimeService.createProcessInstanceQuery()
                    .processDefinitionId(myProcessDefinition.getId())
                    .active() // we only want the unsuspended process instances
                    .list();

    return processInstances;
  }


  public String produceMsg(HttpServletRequest req
          , @RequestParam(value="caseId", defaultValue="") String caseId
          , @RequestParam(value="priority", defaultValue="") String priority

  ) {
      List<ProcessInstance> list = this.getRuntimeService().createProcessInstanceQuery().list();
      List<VariableInstance> asdf = this.getRuntimeService().createVariableInstanceQuery().list();
      return null;
  }
}
