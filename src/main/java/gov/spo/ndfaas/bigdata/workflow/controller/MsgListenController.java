package gov.spo.ndfaas.bigdata.workflow.controller;

import gov.spo.ndfaas.bigdata.workflow.service.HandleProcessService;
import gov.spo.ndfaas.bigdata.workflow.dto.TrgtCaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MsgListenController {

    private final HandleProcessService handleProcessService;

    @JmsListener(destination = "trgtCase", containerFactory = "myFactory")
    public void receiveMessage(TrgtCaseDto trgtCaseDto) {

        log.debug("##@# JMS receiveMessage {}.{}", this.getClass().getName(), trgtCaseDto.toString());

        ProcessInstance pi = this.handleProcessService.startProcess(trgtCaseDto.getProcessId(), trgtCaseDto.getCaseId(), trgtCaseDto.getPriority());

        log.debug("##@# start process {}", pi.toString());
    }

}
