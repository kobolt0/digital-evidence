package kr.go.spo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 테이블 Tb_TASK_RUN_HST DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRunHstDto {
    public String seq;
    public String processInstanceId;
    public String taskInstanceId;
    public String caseId;
    public String taskStatus;
    public String taskStartTime;
    public String taskSuspendTime;
    public String taskEndTime;
    public String updateTime;
}
