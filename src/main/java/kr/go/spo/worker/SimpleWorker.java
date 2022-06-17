package kr.go.spo.worker;

import com.google.gson.Gson;
import kr.go.spo.common.HttpUtils;
import kr.go.spo.common.HttpResVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.commons.utils.StringUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class SimpleWorker extends CommonWorker {

    public SimpleWorker(SqlSessionTemplate sqlSessionTemplate) {
        super(sqlSessionTemplate);
    }


    //모델러에 정의된 rest api URI 변수명
    final String keyApiUri = "processApiUri";

    @Value("${workflow.preprocess.serverIp}")
    String apiServerIp;

    // 개별로직 구현
    @Override
    public String excuteMain(DelegateExecution execution) {

        log.debug("#@## END excuteMain ################################");
        log.debug("#@## taskNm: " + execution.getBpmnModelElementInstance().getName());
        log.debug("#@## inParamMap " + inParamMap);
        log.debug("#@## getActivityInstanceId: " + execution.getActivityInstanceId());
        log.debug("#@## ### ########## ################################");

        // rest api 호출
        log.debug("#@## HttpUtils.callHttpGet[{}]", apiServerIp + inParamMap.get(this.keyApiUri));
        HttpResVo resVO = HttpUtils.callHttpGet(apiServerIp + inParamMap.get(this.keyApiUri));  //호출
        log.debug("#@## HttpUtils.callHttpGet END getResponsCode:[{}]  getResponsCode[{}]  getContent[{}]", resVO.getResponsCode(), resVO.getResponsCode(), resVO.getContent());
        String resultStr = resVO.getResponsCode() == 200 ? "정상종료" : "오류";

        // Gson 객체 생성
        Gson gson = new Gson();

        // rest api 에서 받은 값를 그대로 워크플로 파라메터에 설정
        // Json 문자열 -> Map
        String strContent = resVO.getContent();
        if (StringUtils.isNotEmpty(strContent)){
            Map<String, Object> jsonMap = gson.fromJson(strContent, Map.class);
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                execution.setVariable(entry.getKey(), entry.getValue());
            }
        }

        log.debug("#@## END excuteMain ################################");
        log.debug("#@## taskNm: " + execution.getBpmnModelElementInstance().getName());
        log.debug("#@## vals " + execution.getVariables().toString());
        log.debug("#@## getActivityInstanceId: " + execution.getActivityInstanceId());
        log.debug("#@## ### ########## ################################");

        return resultStr;
    }


}