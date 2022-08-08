package kr.go.spo.config;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.incident.IncidentHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IncidentHandlerProcessEnginePlugin  extends AbstractProcessEnginePlugin {


	
	@Override
	  public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

	    List<IncidentHandler> customIncidentHandlers = new ArrayList<>();
	    customIncidentHandlers.add(new FailedJobIncidentHandler());
	    processEngineConfiguration.setCustomIncidentHandlers(customIncidentHandlers );

	}
}