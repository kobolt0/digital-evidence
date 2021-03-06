package kr.go.spo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrgtCaseDto implements Serializable {
  public String caseId;
  public String priority;
}
