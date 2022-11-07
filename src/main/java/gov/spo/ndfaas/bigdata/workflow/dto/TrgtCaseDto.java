package gov.spo.ndfaas.bigdata.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrgtCaseDto implements Serializable {
  public String processId;
  public String caseId;
  public String priority;
}
