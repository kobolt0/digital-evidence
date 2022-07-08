package kr.go.spo.common;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * HTTP 관련 유틸리티 공통 메소드
 */
@Slf4j
public class HttpUtils {

    static final int connTimeout = 50000;
    static final int readTimeout = 30000;



    public static HttpResVo callHttpGet(String strUrl)
    {
        log.debug("##@# 한글 callHttpGet[{}]", strUrl);
        URL url;
        String readLine;
        StringBuilder buffer;
        BufferedReader bufferedReader = null;
        HttpURLConnection urlConnection;

        HttpResVo result = new HttpResVo();

        try
        {
            url = new URL(strUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(connTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setRequestProperty("Accept", "application/json;");

            buffer = new StringBuilder();
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                while((readLine = bufferedReader.readLine()) != null)
                {
                    if (buffer.length() > 0){
                        buffer.append("\n");
                    }
                    buffer.append(readLine);
                }

            }
            result.setContent(buffer.toString());
            result.setResponsCode(urlConnection.getResponseCode());
            result.setResponsMsg(urlConnection.getResponseMessage());

            buffer.append("code : ");
            buffer.append(urlConnection.getResponseCode()).append("\n");
            buffer.append("message : ");
            buffer.append(urlConnection.getResponseMessage()).append("\n");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (bufferedReader != null) { bufferedReader.close(); }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String map2GetParam(Map<String, String> inMap)
    {
        if (inMap == null) return "";

        StringBuffer sb = new StringBuffer();
        Set<String> set = inMap.keySet();
        for (String key :set) {
            sb.append(sb.length() > 0 ? "&" : "?");
            sb.append(key).append("=").append(inMap.get(key));
        }

        return sb.toString();
    }
}
