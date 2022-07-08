package kr.go.spo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 테이블 Tb_TASK_RUN VO
 */
@Data
@AllArgsConstructor
public class ProcessInstanceDto {
    public String processInstanceId;
    public String caseId;
    public String status;
    public String startTime;
    public String suspendTime;
    public String endTime;
}
