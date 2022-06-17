package kr.go.spo.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    public static void main(String[] args) throws Exception {

//		apiTestGet();
        String apiUrl = "http://localhost:8070/engine-rest/task";	// 각자 상황에 맞는 IP & url 사용
		apiUrl = "http://localhost:8070/engine-rest/process-definition/key/test/start";

        HttpResVo result = callHttpGet(apiUrl + map2GetParam(null));
        System.out.println(result);
    }


    static final int connTimeout = 5000;
    static final int readTimeout = 3000;



    public static HttpResVo callHttpGet(String strUrl)
    {
        URL url = null;
        String readLine = null;
        StringBuilder buffer = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        HttpURLConnection urlConnection = null;

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
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
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
                if (bufferedWriter != null) { bufferedWriter.close(); }
                if (bufferedReader != null) { bufferedReader.close(); }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String map2GetParam(Map<String, String> inMap) throws Exception
    {
        if (inMap == null) return "";

        StringBuffer bf = new StringBuffer();
        Set<String> keyset = inMap.keySet();
        for (String key :keyset) {
            bf.append(bf.length() > 0 ? "&" : "?");
            bf.append(key).append("=").append(inMap.get(key));
        }

        return bf.toString();
    }
}
