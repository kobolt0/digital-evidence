package kr.go.spo.worker;

import kr.go.spo.dto.TaskRunDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.IntegerValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

  protected final SqlSessionTemplate sqlSessionTemplate;

  /** 타스크인스턴스ID */
  protected String taskInstanceId = null;

  /** 프로세스인스턴스ID */
  protected String processInstanceId = null;

  /** 입력파라메터 Map (워크플로우에서 입력받은) */
  Map<String, String> inParamMap = null;

  /** 압축해재수행 횟수 */
  protected int extractCnt = 0;

  /** 압축반복 종료 여부 */
  protected boolean extractEndYn = false;


  /**
   * [압축해재수행횟수]를 변경하고, [압축반복 종료 여부]를 갱신
   */
  protected void increaseExtractCnt(int extractCnt) {
    this.extractCnt = extractCnt;
    this.extractEndYn = (extractCnt >= this.extractMaxCnt);
  }
  @Value("${workflow.preprocess.extractMaxCnt}")
  int extractMaxCnt ;
  @Value("${workflow.preprocess.extractMaxCnt}")
  String strextractMaxCnt ;

  abstract protected String excuteMain(DelegateExecution execution) throws Exception;


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
    log.debug("#@##################################");
    log.debug("\n");



    // 1. 초기화
    // 입력파라메터 Map 설정
    inParamMap = new HashMap<>();
    Set <String> paramNames = execution.getVariableNames();
    for (String name : paramNames) {
      if(execution.getVariable(name) == null){
        inParamMap.put(name, null);
      }
      else{
        TypedValue tval = execution.getVariableTyped(name);
        inParamMap.put(name, tval.getValue().toString());
      }
    }


    // 인스턴스id 설정
    this.processInstanceId = execution.getProcessInstanceId();
    this.taskInstanceId = execution.getActivityInstanceId();

    // 압축해재수행 횟수 설정
    IntegerValue ivExCnt = execution.getVariableTyped("extractCnt");
    if (ivExCnt != null) {
      increaseExtractCnt(ivExCnt.getValue());
    }

    log.debug("\n");
    log.debug("\n");
    log.debug("##@#  inParamMap.toString(): " + inParamMap.toString());

    // 2. 타스크 시작 상태 db Insert
    TaskRunDto taskRunVO;
    taskRunVO = new TaskRunDto(
            "" + this.taskInstanceId
            , "" + inParamMap.get("caseId")
            , "" + this.processInstanceId
            , "시작"
            , "" + new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date())
            , null
    );


    log.debug("#@## insertTbTaskRun[{}]",  taskRunVO);
    int insRslt = sqlSessionTemplate.insert("preprocess.insertTbTaskRun", taskRunVO);
    log.debug("##@# insRslt[{}]", insRslt);

    // 3. 개별로직 호출
    String rslt = this.excuteMain(execution);
    log.debug("##@# excuteMain return[{}]", rslt);

    // 4. 타스크 종료 상태 db Update
    taskRunVO = new TaskRunDto(
            "" + this.taskInstanceId
            , "" + this.processInstanceId
            , "" + inParamMap.get("caseId")
            , rslt
            , null
            , "" + new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date())
    );

    log.debug("#@## this.taskInstanceId[{}]" +  this.taskInstanceId);


    log.debug("#@## updateTbTaskRun[{}]" ,  taskRunVO);
    int updRslt = sqlSessionTemplate.update("preprocess.updateTbTaskRun", taskRunVO);
    log.debug("##@# updRslt[{}]", updRslt);

    // 4. 리턴 파라메터 설정
    execution.setVariable("extractCnt", this.extractCnt);
    execution.setVariable("extractEndYn", this.extractEndYn);

    log.debug("\n");
    log.debug("#@## END CommonWorker ############################################");
    log.debug("##@#  taskName: " + execution.getBpmnModelElementInstance().getName());
    log.debug("##@#  vals " + execution.getVariables().toString());
    log.debug("#@##################################");
    log.debug("\n");
  }
}