package kr.go.spo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TbFile {
  public String fileId;
  public String caseId;
  public String filePath;
  public String fileSize;
  public String createTime;
  public String firstCate;
  public String signatureCate;
}
