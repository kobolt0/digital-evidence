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


    final String keyApiUri = "processApiUri";

    @Value("${workflow.preprocess.serverIp}")
    String apiServerIp;

    @Override
    public String excuteMain(DelegateExecution execution) {

        // rest api 호출
        log.info("# ## HttpUtils.callHttpGet[{}]", apiServerIp + inParamMap.get(this.keyApiUri));
        HttpResVo resVO = HttpUtils.callHttpGet(apiServerIp + inParamMap.get(this.keyApiUri));
        String resultStr = resVO.getResponsCode() == 200 ? "정상종료" : "오류";

        // Gson 객체 생성
        Gson gson = new Gson();

        // Json 문자열 -> Map
        String strContent = resVO.getContent();
        if (StringUtils.isNotEmpty(strContent)){
            Map<String, Object> jsonMap = gson.fromJson(strContent, Map.class);
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                execution.setVariable(entry.getKey(), entry.getValue());
            }
        }

        log.info("# ## excuteMain ################################");

        //log.info("# ## jsonMap[{}]", jsonMap);
        log.info("# ## this.taskInstanceId[{}]", this.taskInstanceId);
        log.info("# ## this.sqlSessionTemplate[{}]", this.sqlSessionTemplate);
        log.info("# ## this.inParamMap[{}]", this.inParamMap);

        log.info("# ## execution.getBpmnModelElementInstance().getName(): " + execution.getBpmnModelElementInstance().getName());
        log.info("# ## vals " + execution.getVariables().toString());
        log.info("# ## getProcessInstanceId: " + execution.getProcessInstanceId());
        log.info("# ## getActivityInstanceId: " + execution.getActivityInstanceId());
        log.info("# ## getCurrentActivityId: " + execution.getCurrentActivityId());
        log.info("# ## getId: " + execution.getId());

        return resultStr;
    }


}