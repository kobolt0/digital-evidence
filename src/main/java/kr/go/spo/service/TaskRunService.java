package kr.go.spo.service;

import kr.go.spo.dto.TaskRunDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRunService {
    private final SqlSessionTemplate sqlSessionTemplate;


    public int insertTbTaskRun(TaskRunDto taskRunDto) {
        int cudRslt = sqlSessionTemplate.update("preprocess.insertTbTaskRun", taskRunDto);
        log.debug("##@# cudRslt.{}", cudRslt);
        return cudRslt;
    }

    public int mergeTbTaskRun(TaskRunDto taskRunDto) {
        int cudRslt = sqlSessionTemplate.update("preprocess.mergeTbTaskRun", taskRunDto);
        log.debug("##@# cudRslt.{}", cudRslt);
        return cudRslt;
    }


    public int updateTbTaskRun(TaskRunDto taskRunDto) {
        int cudRslt = sqlSessionTemplate.update("preprocess.updateTbTaskRun", taskRunDto);
        log.debug("##@# cudRslt.{}", cudRslt);
        return cudRslt;
    }


}
