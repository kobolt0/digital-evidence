package gov.spo.ndfaas.bigdata.workflow.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    public static String now(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String now() {
        return now("yyyyMMddHHmmssS");
    }

}