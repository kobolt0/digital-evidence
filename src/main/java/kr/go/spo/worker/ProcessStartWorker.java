package kr.go.spo.worker;

import kr.go.spo.common.CommonUtils;
import kr.go.spo.dto.ProcessInstanceDto;
import kr.go.spo.service.ProcessInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessStartWorker implements JavaDelegate {

    private final ProcessInstanceService processInstanceService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ProcessInstanceDto processInstanceDto = new ProcessInstanceDto(
                execution.getProcessInstanceId()
                , execution.getBusinessKey()
                , "시작"
                ,""+ CommonUtils.now()
                ,""
                ,""
        ) ;

        //프로세스인스턴스 디비 인서트
        this.processInstanceService.insertTbProcessInstance(processInstanceDto);

        execution.setVariable("processStartTime", processInstanceDto.getStartTime());
        execution.setVariable("processStatus", processInstanceDto.getStatus());



    }
}