package gov.spo.ndfaas.bigdata.workflow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.batch.BatchQuery;
import org.camunda.bpm.engine.management.*;
import org.camunda.bpm.engine.runtime.*;
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
@RequestMapping("/management")
public class ManagementController {

  private ManagementService runtimeService;
  private ManagementService getManagementService(){
    if (this.runtimeService == null){
      this.runtimeService = ProcessEngines.getDefaultProcessEngine().getManagementService();
    }
    return this.runtimeService;
  }


    // JobQuery 목록
    @GetMapping("/jobQuery")
    public List<Map<String, String>> jobQuery(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        JobQuery qry = this.getManagementService().createJobQuery();

        List<Job> list = qry.list();

        for (Job obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getProcessInstanceId());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnMap.put("getProcessDefinitionId", obj.getProcessDefinitionId());
            rtnMap.put("getExecutionId", obj.getExecutionId());
            rtnMap.put("getDeploymentId", obj.getDeploymentId());
            rtnMap.put("getExceptionMessage", obj.getExceptionMessage());
            rtnMap.put("getFailedActivityId", obj.getFailedActivityId());
            rtnMap.put("getJobDefinitionId", obj.getJobDefinitionId());
            rtnMap.put("getProcessDefinitionKey", obj.getProcessDefinitionKey());
            rtnMap.put("getCreateTime", ObjectUtils.defaultIfNull(obj.getCreateTime(),"").toString());
            rtnMap.put("getDuedate", ObjectUtils.defaultIfNull(obj.getDuedate(),"").toString());
            rtnMap.put("getPriority", ObjectUtils.defaultIfNull(obj.getPriority(),"").toString());
            rtnMap.put("getRetries", ObjectUtils.defaultIfNull(obj.getRetries(),"").toString());
            rtnList.add(rtnMap);
        }
        return rtnList;
    }


    // batchQuery 목록
    @GetMapping("/batchQuery")
    public List<Map<String, String>> batchQuery(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        BatchQuery qry = this.getManagementService().createBatchQuery();

        List<Batch> list = qry.list();

        for (Batch obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getProcessInstanceId", obj.getBatchJobDefinitionId());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnMap.put("getCreateUserId", obj.getCreateUserId());
            rtnMap.put("getType", obj.getType());
            rtnMap.put("getMonitorJobDefinitionId", obj.getMonitorJobDefinitionId());
            rtnMap.put("getSeedJobDefinitionId", obj.getSeedJobDefinitionId());
            rtnMap.put("getBatchJobsPerSeed", obj.getBatchJobsPerSeed() + "");
            rtnMap.put("getInvocationsPerBatchJob", obj.getInvocationsPerBatchJob()+"");
            rtnMap.put("getJobsCreated", obj.getJobsCreated()+"");
            rtnMap.put("getTotalJobs", String.valueOf(obj.getTotalJobs()));

            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // activityStatisticsQuery 목록
    @GetMapping("/activityStatisticsQuery")
    public List<Map<String, String>> activityStatisticsQuery(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        ActivityStatisticsQuery qry = this.getManagementService().createActivityStatisticsQuery(val1); //processDefinitionId
        List<ActivityStatistics> list = qry.list();

        for (ActivityStatistics obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getInstances", String.valueOf(obj.getInstances()));
            rtnMap.put("getFailedJobs", String.valueOf(obj.getFailedJobs()));
            rtnMap.put("getIncidentStatistics", String.valueOf(obj.getIncidentStatistics()));
            rtnMap.put("getIncidentStatisticsSize", String.valueOf(obj.getIncidentStatistics().size()));

            rtnList.add(rtnMap);
        }
        return rtnList;
    }

   // jobDefinitionQuery 목록
    @GetMapping("/jobDefinitionQuery")
    public List<Map<String, String>> jobDefinitionQuery(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        JobDefinitionQuery qry = this.getManagementService().createJobDefinitionQuery();

        List<JobDefinition> list = qry.list();

        for (JobDefinition obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getActivityId", obj.getActivityId());
            rtnMap.put("getDeploymentId", obj.getDeploymentId());
            rtnMap.put("getProcessDefinitionId", obj.getProcessDefinitionId());
            rtnMap.put("getJobType", obj.getJobType());
            rtnMap.put("getProcessDefinitionKey", obj.getProcessDefinitionKey());
            rtnMap.put("getTenantId", obj.getTenantId());
            rtnMap.put("getOverridingJobPriority", String.valueOf(obj.getOverridingJobPriority()));
            rtnList.add(rtnMap);
        }
        return rtnList;
    }

    // processDefinitionStatisticsQuery 목록
    @GetMapping("/processDefinitionStatisticsQuery")
    public List<Map<String, String>> processDefinitionStatisticsQuery(HttpServletRequest req, @RequestParam(value = "val1", defaultValue = "") String val1) {
        log.debug("##@# REST. {}", req.getRequestURL());
        List<Map<String, String>> rtnList = new ArrayList<>();
        ProcessDefinitionStatisticsQuery qry = this.getManagementService().createProcessDefinitionStatisticsQuery();

        List<ProcessDefinitionStatistics> list = qry.list();

        for (ProcessDefinitionStatistics obj : list) {
            Map<String, String> rtnMap = new LinkedHashMap<>();
            rtnMap.put("getId", obj.getId());
            rtnMap.put("getCategory", obj.getCategory());
            rtnMap.put("getDeploymentId", obj.getDeploymentId());
            rtnMap.put("getName", obj.getName());
            rtnMap.put("getDescription", obj.getDescription());
            rtnMap.put("getDiagramResourceName", obj.getDiagramResourceName());
            rtnMap.put("getKey", obj.getKey());
            rtnMap.put("getResourceName", obj.getResourceName());
            rtnMap.put("getVersionTag", obj.getVersionTag());
            rtnMap.put("getVersion", String.valueOf(obj.getVersion()));
            rtnMap.put("getFailedJobs", String.valueOf(obj.getFailedJobs()));
            rtnMap.put("getIncidentStatistics", String.valueOf(obj.getIncidentStatistics()));
            rtnMap.put("getHistoryTimeToLive", String.valueOf(obj.getHistoryTimeToLive()));
            rtnMap.put("getIncidentStatistics", String.valueOf(obj.getIncidentStatistics()));

            rtnList.add(rtnMap);
        }
        return rtnList;
    }


}
