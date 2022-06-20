package kr.go.spo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 테이블 Tb_TASK_RUN VO
 */
@Data
@AllArgsConstructor
public class TbTaskRun {
  public String taskInstanceId;
  public String processInstanceId;
  public String caseId;
  public String taskStatus;
  public String taskStartTime;
  public String taskEndTime;
}
