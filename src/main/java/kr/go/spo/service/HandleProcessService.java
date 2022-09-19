package kr.go.spo.service;

import kr.go.spo.common.CommonDao;
import kr.go.spo.dto.ProcessInstanceDto;
import kr.go.spo.dto.TaskRunDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HandleProcessService {

  private RuntimeService runtimeService;
  private final CommonDao commonDao;

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
  public ProcessInstance startProcess(String processId, String caseId, String prioty) {
    Map<String, Object> variables=new HashMap<>();
    variables.put("caseId",caseId);
    variables.put("prioty",prioty);

    ProcessInstanceWithVariables instance = this.getRuntimeService().createProcessInstanceByKey(processId)
            .setVariables(variables)
            .businessKey(caseId)
            .executeWithVariablesInReturn()
            ;
    return instance;
  }


    public void chkSuspendedProcess() {
        log.debug("##@# {}" , this.getClass().getName());
        List<TaskRunDto> list = this.commonDao.selectList("selectListChkSuspendedProcess", null);
        log.debug("##@# chkSuspendedProcess size.{}", list.size());

        for (TaskRunDto dto : list) {

            //프로세스가 정지상태인지 확인
            boolean cond = this.getRuntimeService().createProcessInstanceQuery()
                    .processInstanceId(dto.getProcessInstanceId())
                    .singleResult()
                    .isSuspended();
            log.debug("##@# isResumeTrg? {} pid[{}]", cond, dto.getProcessInstanceId());
            if (cond){
                this.getRuntimeService().activateProcessInstanceById(dto.getProcessInstanceId());
                log.debug("##@# activateProcessInstance id[{}]", dto.getProcessInstanceId());
            }
        }
    }
}
