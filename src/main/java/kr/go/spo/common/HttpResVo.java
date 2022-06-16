package kr.go.spo.common;

import lombok.Data;

@Data
public class HttpResVo {
    private String content;
    private int responsCode;
    private String responsMsg;
}
