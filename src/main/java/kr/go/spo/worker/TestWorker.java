package kr.go.spo.worker;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestWorker implements JavaDelegate {

  public void execute(DelegateExecution execution) {

//		IntegerValue val1 = execution.getVariableTyped("val1");


    VariableMap asdf = execution.getVariablesLocalTyped();

    execution.setVariable("approved", true);

    log.debug("\n\n#@##################################");
    log.debug("##@# execution.getBpmnModelElementInstance().getName(): " + execution.getBpmnModelElementInstance().getName());
    log.debug("##@# vals " + execution.getVariables().toString());
    log.debug("##@# getActivityInstanceId: " + execution.getActivityInstanceId());
    log.debug("##@# getCurrentActivityId: " + execution.getCurrentActivityId());
    log.debug("##@# getId: " + execution.getId());

    log.debug("\n\n#@##################################" + this.getClass().getName());
    log.debug("\n\n#@##################################");


  }

}