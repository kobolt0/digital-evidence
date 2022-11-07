package gov.spo.ndfaas.bigdata.workflow.controller;

import gov.spo.ndfaas.bigdata.workflow.service.HandleProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
import java.util.*;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class HandleProcessController {

  private RuntimeService runtimeService;
  private final HandleProcessService handleProcessService;

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


    // 인시던트 목록
      @GetMapping("/incidentQry")
    public List<Map<String, String>> incidentQry(HttpServletRequest req, @RequestParam(value="val1", defaultValue="") String val1) {
          // 인시던트 쿼리 테스트
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();

        List<Incident> listInc = this.getRuntimeService().createIncidentQuery()
                .list();
        log.debug("");
        log.debug("##@#Incident ");
        for (Incident inc : listInc) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", inc.getId());
            rtnMap.put("getProcessInstanceId", inc.getProcessInstanceId());
            rtnMap.put("getCauseIncidentId", inc.getCauseIncidentId());
            rtnMap.put("getActivityId", inc.getActivityId());
            rtnMap.put("getIncidentType", inc.getIncidentType());
            rtnMap.put("getAnnotation", inc.getAnnotation());
            rtnMap.put("getConfiguration", inc.getConfiguration());
            rtnMap.put("getExecutionId", inc.getExecutionId());
            rtnMap.put("getFailedActivityId", inc.getFailedActivityId());
            rtnMap.put("getIncidentMessage", inc.getIncidentMessage());
            rtnMap.put("getHistoryConfiguration", inc.getHistoryConfiguration());
            rtnMap.put("getJobDefinitionId", inc.getJobDefinitionId());
            rtnMap.put("getProcessDefinitionId", inc.getProcessDefinitionId());
            rtnMap.put("getRootCauseIncidentId", inc.getRootCauseIncidentId());
            rtnMap.put("getIncidentTimestamp", inc.getIncidentTimestamp().toString());
            rtnMap.put("getTenantId", inc.getTenantId());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // Execution 목록
    @GetMapping("/executionQry")
    public List<Map<String, String>> executionQry(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        // Execution 쿼리 테스트
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();

        ExecutionQuery qry = this.getRuntimeService().createExecutionQuery();

        List<Execution> list = qry.list();

        log.debug("");
        log.debug("##@#executionQry ");
        for (Execution obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getProcessInstanceId());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // ProcessInstance 목록
    @GetMapping("/processInstanceQry")
    public List<Map<String, String>> processInstanceQry(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        // ProcessInstance 쿼리 테스트
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();

        ProcessInstanceQuery qry = this.getRuntimeService().createProcessInstanceQuery();

        List<ProcessInstance> list = qry.list();

        log.debug("");
        log.debug("##@# processInstanceQ ");
        for (ProcessInstance obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getProcessInstanceId());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnMap.put("getCaseInstanceId", obj.getCaseInstanceId());
            rtnMap.put("getRootProcessInstanceId", obj.getRootProcessInstanceId());
            rtnMap.put("getBusinessKey", obj.getBusinessKey());
            rtnMap.put("getProcessDefinitionId", obj.getProcessDefinitionId());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // VariableInstance 목록
    @GetMapping("/variableInstanceQry")
    public List<Map<String, String>> variableInstanceQry(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        // VariableInstance 쿼리 테스트
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();

        VariableInstanceQuery qry = this.getRuntimeService().createVariableInstanceQuery();

        List<VariableInstance> list = qry.list();

        log.debug("");
        log.debug("##@# processInstanceQ ");
        for (VariableInstance obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getProcessInstanceId());
            rtnMap.put("getExecutionId", obj.getExecutionId());
            rtnMap.put("getName", obj.getName());
            rtnMap.put("getTypeName", obj.getTypeName());
            rtnMap.put("getTypedValue", obj.getTypedValue().toString());
            rtnMap.put("getValue", ObjectUtils.defaultIfNull(obj.getValue(), "").toString());
            rtnMap.put("getTaskId", obj.getTaskId());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnMap.put("getCaseInstanceId", obj.getCaseInstanceId());
            rtnMap.put("getActivityInstanceId", obj.getActivityInstanceId());
            rtnMap.put("getBatchId", obj.getBatchId());
            rtnMap.put("getProcessDefinitionId", obj.getProcessDefinitionId());
            rtnMap.put("getCaseExecutionId", obj.getCaseExecutionId());
            rtnMap.put("getErrorMessage", obj.getErrorMessage());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }


    // eventSubscriptionQry 목록
    @GetMapping("/eventSubscriptionQry")
    public List<Map<String, String>> eventSubscriptionQry(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        EventSubscriptionQuery qry = this.getRuntimeService().createEventSubscriptionQuery();

        List<EventSubscription> list = qry.list();

        for (EventSubscription obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getProcessInstanceId());
            rtnMap.put("getExecutionId", obj.getExecutionId());
            rtnMap.put("getEventName", obj.getEventName());
            rtnMap.put("getActivityId", obj.getActivityId());
            rtnMap.put("getEventType", obj.getEventType());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnMap.put("getCreated", obj.getCreated().toString());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // NativeExecutionQuery 목록
    @GetMapping("/nativeExecutionQry")
    public List<Map<String, String>> nativeExecutionQry(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        NativeExecutionQuery qry = this.getRuntimeService().createNativeExecutionQuery();

        List<Execution> list = qry.list();

        for (Execution obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getProcessInstanceId());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // NativeExecutionQuery 목록
    @GetMapping("/nativeProcessInstanceQry")
    public List<Map<String, String>> nativeProcessInstanceQry(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        NativeProcessInstanceQuery qry = this.getRuntimeService().createNativeProcessInstanceQuery();

        List<ProcessInstance> list = qry.list();

        for (ProcessInstance obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getProcessInstanceId());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnMap.put("getProcessDefinitionId", obj.getProcessDefinitionId());
            rtnMap.put("getRootProcessInstanceId", obj.getRootProcessInstanceId());
            rtnMap.put("getBusinessKey", obj.getBusinessKey());
            rtnMap.put("getCaseInstanceId", obj.getCaseInstanceId());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // 진행중인 프로세스 접근

  @GetMapping("/runningProc")
  public String runningProc(HttpServletRequest req, @RequestParam(value="val1", defaultValue="") String val1) {
    log.debug("##@# TEST. {}", req.getRequestURL());
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//    List<ProcessInstance> obj = getAllRunningProcessInstances(val1);
      // 인시던트 테스트
      List<Incident> listInc = this.getRuntimeService().createIncidentQuery()
              .list();
      log.debug("");
      log.debug("##@#Incident " );
      for(Incident inc: listInc){
          log.debug("##@#getId [{}]", inc.getId());
      }



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

//      managementService.suspendJobById();


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

  // 디비상태는 재시작인데 실제로는 멈춰있는 프로세스를 재시작한다.
//  @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    @GetMapping("/chkSuspendedProcess")
  public void chkSuspendedProcess() {
      log.debug("##@# chkSuspendedProcess start");
      try {
          this.handleProcessService.chkSuspendedProcess();
      }
      catch (Exception e){
          e.printStackTrace();
          throw e;
      }

      log.debug("##@# chkSuspendedProcess END");
  }
}
