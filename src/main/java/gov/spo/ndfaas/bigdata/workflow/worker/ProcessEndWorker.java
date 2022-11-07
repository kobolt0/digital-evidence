package gov.spo.ndfaas.bigdata.workflow.worker;

import gov.spo.ndfaas.bigdata.workflow.service.ProcessInstanceService;
import gov.spo.ndfaas.bigdata.workflow.dto.ProcessInstanceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessEndWorker implements JavaDelegate {

    private final ProcessInstanceService processInstanceService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ProcessInstanceDto processInstanceDto = new ProcessInstanceDto(
                execution.getProcessInstanceId()
                , execution.getBusinessKey()
                , "종료"
                ,null
                ,null
                ,"Y"
        ) ;

        //프로세스인스턴스 디비 인서트
        this.processInstanceService.updateTbProcessInstance(processInstanceDto);

    }
}