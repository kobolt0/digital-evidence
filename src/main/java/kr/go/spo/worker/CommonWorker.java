package kr.go.spo.worker;

import kr.go.spo.model.TbTaskRun;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.IntegerValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class CommonWorker implements JavaDelegate {

  protected final SqlSessionTemplate sqlSessionTemplate;

  /** 입력파라메터 Map (워크플로우에서 입력받은) */
  Map<String, String> inParamMap = null;

  /** 압축해재수행 횟수 */
  protected int extractCnt = 0;

  /** 압축반복 종료 여부 */
  protected boolean extractEndYn = false;

  /** 타스크인스턴스ID */
  protected String taskInstanceId = null;

  /** 프로세스인스턴스ID */
  protected String processInstanceId = null;

  protected void setExtractCnt(int extractCnt) {
    this.extractCnt = extractCnt;
    this.extractEndYn = (extractCnt >= WorkerConsts.EXTRACT_MAX_CNT);
  }

  abstract protected String excuteMain(DelegateExecution execution);


  @Override
  public void execute(DelegateExecution execution) {

    log.debug("\n");
    log.debug("#@## START CommonWorker ############################################");
    log.debug("##@#  taskName: " + execution.getBpmnModelElementInstance().getName());
    log.debug("##@#  vals " + execution.getVariables().toString());
    log.debug("##@#  getActivityInstanceId: " + execution.getActivityInstanceId());
    log.debug("##@#  getCurrentActivityId: " + execution.getCurrentActivityId());
    log.debug("##@#  getId: " + execution.getId());
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
      setExtractCnt(ivExCnt.getValue());
    }

    log.debug("\n");
    log.debug("\n");
    log.debug("##@#  inParamMap.toString(): " + inParamMap.toString());

    // 2. 타스크 시작 상태 db Insert
    TbTaskRun taskRunVO = null;
    taskRunVO = new TbTaskRun(
            "" + this.taskInstanceId
            , "" + inParamMap.get("caseId")
            , "" + this.processInstanceId
            , "시작"
            , "" + new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date())
            , null
    );


    log.debug("#@## insertTbTaskRun[{}]" +  taskRunVO);
    int insRslt = sqlSessionTemplate.insert("preProc.insertTbTaskRun", taskRunVO);
    log.debug("##@# insRslt[{}]", insRslt);

    // 3. 개별로직 호출
    String rslt = this.excuteMain(execution);
    log.debug("##@# excuteMain return[{}]", rslt);

    // 출력파라메터 Map (rest 전달)
    Map<String, String> outParamMap = new HashMap<>(this.inParamMap);


    // 4. 타스크 종료 상태 db Update
    taskRunVO = new TbTaskRun(
            "" + this.taskInstanceId
            , "" + this.processInstanceId
            , "" + inParamMap.get("caseId")
            , rslt
            , null
            , "" + new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date())
    );

    log.debug("#@## this.taskInstanceId[{}]" +  this.taskInstanceId);


    log.debug("#@## updateTbTaskRun[{}]" +  taskRunVO);
    int updRslt = sqlSessionTemplate.update("preProc.updateTbTaskRun", taskRunVO);
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