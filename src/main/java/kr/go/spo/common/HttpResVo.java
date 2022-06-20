package kr.go.spo.common;

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
