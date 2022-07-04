package kr.go.spo.controller;

import kr.go.spo.dto.TrgtCase;
import kr.go.spo.service.HandleProcessService;
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
    public void receiveMessage(TrgtCase trgtCase) {

        log.debug("##@# {}.receiveMessage {}", this.getClass().getName(), trgtCase.toString());

        ProcessInstance pi = this.handleProcessService.startProcess(trgtCase.getCaseId(), trgtCase.getPriority());

        log.debug("##@# {}", pi.toString());
    }

}
