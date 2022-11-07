package gov.spo.ndfaas.bigdata.workflow.common;

import lombok.Data;

/**
 * Http response VO
 */
@Data
public class HttpResVo {
    private String content;
    private int responsCode;
    private String responsMsg;
}
