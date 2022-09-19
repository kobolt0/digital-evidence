package kr.go.spo.worker;

import kr.go.spo.common.HttpResVo;
import kr.go.spo.common.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.spin.json.SpinJsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.camunda.spin.DataFormats.json;
import static org.camunda.spin.Spin.S;

@Slf4j
@Service
public class TestWorker implements JavaDelegate {

  @Value("${workflow.preprocess.serverIp}")
  String apiServerIp;

  public void execute(DelegateExecution execution) {

//		IntegerValue val1 = execution.getVariableTyped("val1");


    VariableMap asdf = execution.getVariablesLocalTyped();

    execution.setVariable("approved", true);

    log.debug("\n\n#@##################################");
    log.debug("#@## CLASS[{}]", this.getClass().getName());
    log.debug("##@# execution.getBpmnModelElementInstance().getName(): " + execution.getBpmnModelElementInstance().getName());
    log.debug("##@# vals " + execution.getVariables().toString());
    log.debug("##@# getActivityInstanceId: " + execution.getActivityInstanceId());
    log.debug("##@# getCurrentActivityId: " + execution.getCurrentActivityId());
    log.debug("##@# getId: " + execution.getId());

    // rest api 호출
    String callUrl = apiServerIp + "/test/suspendProcess?processInstanceId=" + execution.getProcessInstanceId();
    log.debug("#@## HttpUtils.callHttpGet[{}]", callUrl);
    HttpResVo resVO = HttpUtils.callHttpGet(callUrl);  //호출
    log.debug("#@## HttpUtils.callHttpGet END getResponsCode:[{}]  getResponsCode[{}]  getContent[{}]", resVO.getResponsCode(), resVO.getResponsCode(), resVO.getContent());
    String resultStr = resVO.getResponsCode() == 200 ? "정상종료" : "오류";



//    SpinJsonNode json = S("{\"customer\": \"Kermit\"}", json());

    log.debug("\n\n#@##################################" + this.getClass().getName());

//    execution.getProcessEngineServices()
//            .getRuntimeService()
//            .updateProcessInstanceSuspensionState()
//            .byProcessInstanceId(execution.getProcessInstanceId())
//            .suspend();

    Execution obj1 = execution.getProcessEngineServices()
            .getRuntimeService()
            .createExecutionQuery()
            .singleResult();


    log.debug("#@##isSuspended.{}", obj1.isSuspended());

    log.debug("\n\n#@##################################");


  }

}