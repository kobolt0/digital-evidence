package kr.go.spo.config;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.incident.IncidentContext;
import org.camunda.bpm.engine.impl.incident.IncidentHandler;
import org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.camunda.bpm.engine.runtime.Incident;

import java.util.List;

@Slf4j
public class FailedJobIncidentHandler implements IncidentHandler {
    	public String getIncidentHandlerType() {
	    return Incident.FAILED_JOB_HANDLER_TYPE;
	}

	@Override
	public Incident handleIncident(IncidentContext arg0, String arg1) {
		
		// Do Stuff here... e.g. Send an Email to Admin
		log.debug("##@# FailedJobIncidentHandler [{}][{}]", arg0, arg1);

		return null;
	}


//	public void resolveIncident(String processDefinitionId, String activityId, String executionId, String jobId) {
//		resolveIncident(processDefinitionId, activityId, executionId, jobId);
//	}

	@Override
	public void deleteIncident(IncidentContext arg0) {
		removeIncident(arg0, false);
	}

	@Override
	public void resolveIncident(IncidentContext arg0) {
		removeIncident(arg0, true);
	}

	protected void removeIncident(IncidentContext context, boolean incidentResolved) {
	    List<Incident> incidents = Context
	        .getCommandContext()
	        .getIncidentManager()
	        .findIncidentByConfiguration(context.getConfiguration());

	    for (Incident currentIncident : incidents) {
	      IncidentEntity incident = (IncidentEntity) currentIncident;
	      if (incidentResolved) {
	        incident.resolve();
	      } else {
	        incident.delete();
	      }
	    }
	}
}
