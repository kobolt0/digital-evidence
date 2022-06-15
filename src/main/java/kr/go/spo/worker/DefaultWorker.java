package kr.go.spo.worker;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultWorker  {


    protected void excuteService() {
        log.info("");
    }
}