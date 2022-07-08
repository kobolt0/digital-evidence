package kr.go.spo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 * 테이블 Tb_TASK_RUN VO
 */
@Data
@AllArgsConstructor
public class TaskRunDto {
    public String processInstanceId;
    public String taskInstanceId;
    public String caseId;
    public String taskStatus;
    public String taskStartTime;
    public String taskSuspendTime;
    public String taskEndTime;
}
