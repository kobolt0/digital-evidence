package kr.go.spo.worker;

import kr.go.spo.common.CommonUtils;
import kr.go.spo.common.HttpResVo;
import kr.go.spo.common.HttpUtils;
import kr.go.spo.dto.ProcessInstanceDto;
import kr.go.spo.dto.TaskRunDto;
import kr.go.spo.service.ProcessInstanceService;
import kr.go.spo.service.TaskRunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 공통적으로 수행되는 로직이 정의된 WORKER 추상클래스.
 * 공통로직은 excute 메소드에 구현
 * 개별로직은 excuteMain 메소드를 오버라이드하여 구현
 * @see org.camunda.bpm.engine.delegate.JavaDelegate
 */
@Slf4j
@Service
@RequiredArgsConstructor
public abstract class CommonWorker implements JavaDelegate {

  protected final ProcessInstanceService processInstanceService;
  protected final TaskRunService taskRunService;


  @Value("8")
  int taskMaxCnt ;//  프로세스 수행갯수 제한

  @Value("${workflow.preprocess.serverIp}")
  String apiServerIp;

  abstract protected String executeMain(DelegateExecution execution) throws Exception;


  /**
   * BPMN의 servise task가 실행하는 메소드
   * 공통적으로 수행되는 로직을 구현
   * @param execution
   */
  @Override
  public void execute(DelegateExecution execution) throws Exception {

    log.debug("\n");
    log.debug("#@## START CommonWorker ############################################");
    log.debug("##@#  taskName: " + execution.getBpmnModelElementInstance().getName());
    log.debug("##@#  vals " + execution.getVariables().toString());
    log.debug("##@#  getActivityInstanceId: " + execution.getActivityInstanceId());
    log.debug("##@#  getCurrentActivityId: " + execution.getCurrentActivityId());
    log.debug("##@#  getCurrentActivityName: " + execution.getCurrentActivityName());
    log.debug("##@#  getId: " + execution.getId());
    log.debug("##@#  getProcessInstanceId: " + execution.getProcessInstanceId());
    log.debug("##@#  getProcessDefinitionId: " + execution.getProcessInstance().getProcessDefinitionId());
    log.debug("##@#  this.toString(): " + this.toString());
    log.debug("#@##################################");
    log.debug("\n");

    //////////////////////////////////////////////////////////////
    // 1. 초기화
    int cudRslt = -1; // cud 결과 저장용 임시변수.
    // 입력파라메터 Map 설정
    Map<String, String> inParamMap = new HashMap<>();
    Set<String> paramNames = execution.getVariableNames();
    for (String name : paramNames) {
      if (execution.getVariable(name) == null) {
        inParamMap.put(name, null);
      } else {
        TypedValue tval = execution.getVariableTyped(name);
        inParamMap.put(name, tval.getValue().toString());
      }
    }
    log.debug("##@#  inParamMap.toString(): " + inParamMap.toString());



    String processInstanceId = execution.getProcessInstanceId();  // 프로세스인스턴스ID
    String taskActivityId = execution.getCurrentActivityId();  // 타스크액티비티ID
    String bizKey = execution.getBusinessKey();  // 비지니스 키

    TaskRunDto inTaskRunDto;


    // #######################################
    // # 타스크 할당숫자 제한 로직 시작
    // 해당타스크에 실행중된 프로세스 인스턴스 숫자가 최대치 초과 :: 현재 프로세스인스턴스 일시정지처리
//    final String keyPriority = "";
    final String keyStartTime = "processStartTime";

    RuntimeService runtimeService;
    runtimeService = execution.getProcessEngine().getRuntimeService();
    log.debug("##@# caseId.{}", runtimeService.getVariables(processInstanceId).toString());

    // 실행중인 작업 목록
    List<Execution> listActive = runtimeService.createExecutionQuery()
            .activityId(execution.getCurrentActivityId())
            .active()
            .list();


    // incident 상태인건 제외
    if (listActive.size() > taskMaxCnt) {
      for(int i = listActive.size()-1; i > -1; i--){
        if(runtimeService.createIncidentQuery().processInstanceId(listActive.get(i).getProcessInstanceId()).list().size() > 0){
          listActive.remove(i);
        }
      } // end for
    }

    log.debug("#@# active instance cnt[{}]. curr inst[{}][{}][{}]",  listActive.size(), taskActivityId, bizKey, processInstanceId);

    if (listActive.size() > taskMaxCnt) {

      // 일시정지 rest api 호출
      String callUrl = apiServerIp + "/test/suspendProcess?processInstanceId=" + execution.getProcessInstanceId();
      log.debug("#@## HttpUtils.callHttpGet[{}]", callUrl);
      HttpResVo resVO = HttpUtils.callHttpGet(callUrl);  //호출
      log.debug("#@## HttpUtils.callHttpGet END getResponsCode:[{}]  getResponsCode[{}]  getContent[{}]", resVO.getResponsCode(), resVO.getResponsCode(), resVO.getContent());
      if (resVO.getResponsCode() != 200){
        throw new Exception();
      }

      inTaskRunDto = new TaskRunDto(
              processInstanceId
              , taskActivityId
              , execution.getActivityInstanceId()
              , inParamMap.get("caseId")
              , "대기"
              , ""
              , CommonUtils.now()
              , ""
              , ""
      );

      TaskRunDto obj = taskRunService.selectTbTaskRunByPk(inTaskRunDto);
      log.debug("##@# TaskRunDto obj.{}", obj);
      if (obj == null ){
        cudRslt = taskRunService.insertTbTaskRun(inTaskRunDto);
        log.debug("#@## 상태:대기 insertTbTaskRun[{}] buzikey[{}] result[{}] param[{}]",execution.getBusinessKey(),execution.getActivityInstanceId(), cudRslt, inTaskRunDto);
      }
      else {
        cudRslt = taskRunService.updateTbTaskRun(inTaskRunDto);
        log.debug("#@## 상태:대기 updateTbTaskRun[{}] buzikey[{}] result[{}] param[{}]",execution.getBusinessKey(),execution.getActivityInstanceId(), cudRslt, inTaskRunDto);
      }
      log.debug("##@# suspend process. bizKey[{}], prc[{}]", execution.getBusinessKey(), execution.getProcessInstanceId());
      return;

    }
    // # 타스크 할당숫자 제한 로직 종료 #########

    ///////////////////////////////


    // 2. 타스크 시작 상태 db Insert
    inTaskRunDto = new TaskRunDto(
             processInstanceId
            , taskActivityId
            , execution.getActivityInstanceId()
            , inParamMap.get("caseId")
            , "시작"
            , CommonUtils.now()
            , null
            , null
            , null
    );


    log.debug("#@## start TASK", inTaskRunDto);
//    int insRslt = this.taskRunService.mergeTbTaskRun(inTaskRunDto);

//    log.debug("##@# insRslt[{}]", insRslt);

    TaskRunDto selectObj = taskRunService.selectTbTaskRunByPk(inTaskRunDto);
    log.debug("##@# TaskRunDto obj.{}", selectObj);
    if (selectObj == null ){
      cudRslt = taskRunService.insertTbTaskRun(inTaskRunDto);
      log.debug("#@## 상태:시작 insertTbTaskRun[{}] result[{}] param[{}]",execution.getActivityInstanceId(), cudRslt, inTaskRunDto);
    }
    else {
      cudRslt = taskRunService.updateTbTaskRun(inTaskRunDto);
      log.debug("#@## 상태:시작 updateTbTaskRun[{}] result[{}] param[{}]",execution.getActivityInstanceId(), cudRslt, inTaskRunDto);
    }


    // 3. 개별로직 호출
    String rslt;
    try {
      rslt = this.executeMain(execution);
    } catch (Exception e) {
      log.error("## 오류", e);
      rslt = "오류";
//      execution.getProcessEngine().getRuntimeService().suspendProcessInstanceById(execution.getProcessInstanceId());
      throw e;
    }

    log.debug("##@# executeMain[{}] return[{}]",execution.getActivityInstanceId(), rslt);

    // 4. 타스크 종료 상태 db Update
    inTaskRunDto = new TaskRunDto(
              processInstanceId
            , taskActivityId
            , execution.getActivityInstanceId()
            , inParamMap.get("caseId")
            , rslt
            , null
            , null
            , CommonUtils.now()
            , null
    );

    cudRslt = this.taskRunService.updateTbTaskRun(inTaskRunDto);
    log.debug("#@## 상태:종료 updateTbTaskRun[{}] buzikey[{}] result[{}] param[{}]",execution.getBusinessKey(),execution.getActivityInstanceId(), cudRslt, inTaskRunDto);





    // ###############################################
    // # 대기중인 프로세스 중에 하나뽑아서 재시작 처리
    // 경우 1. 해당타스크의 잡숫자가 최대치 미만 >> 우선순위로 인한 일시정지상태의 프로세스중에서 가장 우선순위가 놓은 것을 재실행

    // 대기중인 작업 목록
    List<Execution> listSuspended = runtimeService.createExecutionQuery()
            .activityId(execution.getCurrentActivityId())
            .suspended()
//            .variableValueEquals("processStatus","대기")
            .variableValueNotEquals("processStatus","사용자임의정지")
            .list();
    // 최우선 프로세스 뽑아내기

    log.debug("##@# listSuspended[{}] cnt:{}", execution.getActivityInstanceId(), listSuspended.size());

    String trgtInstId;  // 처리대상 프로세스인스턴스 아이디
    Execution execPriv;  // 이전 Execution 임시객체 (정렬 용도)

    trgtInstId = null;
    execPriv = null;

    for (Execution execCurr :
            listSuspended) {
      if (execPriv == null){
        log.debug("##@# resume prc.");
        execPriv = execCurr;
        trgtInstId = execCurr.getProcessInstanceId();
      }
      else {
        // 비교기준문자열
        String criterionPriv = runtimeService.getVariable(execPriv.getProcessInstanceId(), keyStartTime).toString();
        String criterionCurr = runtimeService.getVariable(execCurr.getProcessInstanceId(), keyStartTime).toString();
        log.debug("##@# criterionPriv[{}]", criterionPriv);
        log.debug("##@# criterionCurr[{}]", criterionCurr);

        if (criterionCurr.compareTo(criterionPriv) < 0 ){
          trgtInstId = execCurr.getProcessInstanceId();
          log.debug("##@# win criterionCurr[{}]", criterionCurr);

        }
        else {
          trgtInstId = execPriv.getProcessInstanceId();
          log.debug("##@# win criterionPriv[{}]", criterionPriv);
        }
      }
    } // end foreach

    // 최우선 프로세스 재시작
    if (trgtInstId != null){
      log.debug("##@# resume process[{}]", trgtInstId);
      runtimeService.activateProcessInstanceById(trgtInstId);

      // 프로세스 상태 디비업데이트
      ProcessInstanceDto processInstanceDto = new ProcessInstanceDto(
              trgtInstId
              , null
              , "재시작"
              ,null
              ,null
              ,null
      ) ;

      //프로세스인스턴스 디비 인서트
      this.processInstanceService.updateTbProcessInstance(processInstanceDto);
    }


    // # 대기중인 프로세스 중에 하나뽑아서 재시작 처리 로직 종료 #######################

    log.debug("\n");
    log.debug("#@## END CommonWorker ############################################");
    log.debug("##@#  taskName: " + execution.getBpmnModelElementInstance().getName());
    log.debug("##@#  vals " + execution.getVariables().toString());
    log.debug("#@##################################");
    log.debug("\n");
  }

}