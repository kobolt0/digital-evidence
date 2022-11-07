package gov.spo.ndfaas.bigdata.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 테이블 Tb_TASK_RUN DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRunDto {
    public String processInstanceId;
    public String taskActivityId;
    public String taskInstanceId;
    public String caseId;
    public String taskStatus;
    public String taskStartTime;
    public String taskSuspendTime;
    public String taskEndTime;
    public String updateTime;
}
