package kr.go.spo.worker;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestWorker implements JavaDelegate {

  public void execute(DelegateExecution execution) {

//		IntegerValue val1 = execution.getVariableTyped("val1");

    execution.setVariable("approved", true);


    log.info("\n\n##  #################################");
    log.info("##  execution.getBpmnModelElementInstance().getName(): " + execution.getBpmnModelElementInstance().getName());
    log.info("##  vals " + execution.getVariables().toString());
    log.info("##  getActivityInstanceId: " + execution.getActivityInstanceId());
    log.info("##  getCurrentActivityId: " + execution.getCurrentActivityId());
    log.info("##  getId: " + execution.getId());

    log.info("\n\n##  #################################" + this.getClass().getName());
    log.info("\n\n##  #################################");


  }

}