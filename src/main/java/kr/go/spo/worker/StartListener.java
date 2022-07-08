package kr.go.spo.worker;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StartListener implements ExecutionListener {

  public void notify(DelegateExecution execution) {

//		IntegerValue val1 = execution.getVariableTyped("val1");


    VariableMap asdf = execution.getVariablesLocalTyped();

    execution.setVariable("approved", true);

    log.debug("\n\n#@##################################");
    log.debug("#@## CLASS[{}]", this.getClass().getName());
    log.debug("##@# getActivityInstanceId: " + execution.getActivityInstanceId());
    log.debug("##@# getCurrentActivityId: " + execution.getCurrentActivityId());
    log.debug("##@# getId: " + execution.getId());


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
    boolean sadf = obj1.isSuspended();




    log.debug("#@##isSuspended.{}", obj1.isSuspended());
    log.debug("\n\n#@##################################");


  }

}