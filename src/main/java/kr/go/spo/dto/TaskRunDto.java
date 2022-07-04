package kr.go.spo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 테이블 Tb_TASK_RUN VO
 */
@Data
@AllArgsConstructor
public class TaskRunDto {
  public String taskInstanceId;
  public String processInstanceId;
  public String caseId;
  public String taskStatus;
  public String taskStartTime;
  public String taskEndTime;
}
