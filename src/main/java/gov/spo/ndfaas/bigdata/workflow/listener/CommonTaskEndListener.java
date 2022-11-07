package gov.spo.ndfaas.bigdata.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommonTaskEndListener implements ExecutionListener {

  public void notify(DelegateExecution execution) {

    log.debug("\n\n#@##################################");
    log.debug("#@## CLASS[{}]", this.getClass().getName());
    log.debug("##@# getActivityInstanceId: [{}]", execution.getActivityInstanceId());
    log.debug("##@# getId: [{}]", execution.getId());
    log.debug("##@# isCanceled: [{}]", execution.isCanceled());

    RuntimeService runtimeService = execution.getProcessEngine().getRuntimeService();
    ActivityInstance obj1 = runtimeService.getActivityInstance(execution.getProcessInstanceId());
    ProcessInstance obj2 = runtimeService.createProcessInstanceQuery()
            .processInstanceId(execution.getProcessInstanceId())
            .singleResult();
    Execution obj3 = runtimeService.createExecutionQuery()
            .processInstanceId(execution.getProcessInstanceId())
            .singleResult();

    log.debug("##@# getCurrentActivityId: [{}]", execution.getCurrentActivityId());
    log.debug("##@# getProcessBusinessKey: [{}]", execution.getProcessBusinessKey());
    log.debug("##@# obj2 isCanceled: [{}]", obj2.isSuspended());
    log.debug("##@# obj3 isCanceled: [{}]", obj3.isSuspended());



    log.debug("\n\n#@##################################" + this.getClass().getName());

    // # 대기상태 확인후 디비값 변경. 워커에서 디비상태값을 대기상태로 변경하기가 어렵기(프로세스를 정지하면 워커에서 수행된부분이 롤백이 일어남)때문에 대기상태로 상태값업데이트는 여기서 구현

    // 프로세스 대기상태 확인

    // 디비 대기상태 확인

    // 프로세스는 대기상태인대, 디비값이 진행상태이면 대기상태로 없대이트


//    execution.getProcessEngineServices()
//            .getRuntimeService()
//            .updateProcessInstanceSuspensionState()
//            .byProcessInstanceId(execution.getProcessInstanceId())
//            .suspend();




  }

}