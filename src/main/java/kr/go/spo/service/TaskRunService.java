package kr.go.spo.service;

import kr.go.spo.common.CommonDao;
import kr.go.spo.dto.TaskRunDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRunService {
    private final CommonDao commonDao;


    public TaskRunDto selectTbTaskRunByPk(TaskRunDto taskRunDto) {
        TaskRunDto obj = commonDao.selectOne("preprocess.selectTbTaskRunByPk", taskRunDto);
        return obj;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
    public int insertTbTaskRun(TaskRunDto taskRunDto) {
        int cudRslt = commonDao.insert("preprocess.insertTbTaskRun", taskRunDto);
        commonDao.insert("preprocess.insertTbTaskRunHstByPk", taskRunDto);
//        log.debug("##@# @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)");
        return cudRslt;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
    public int mergeTbTaskRun(TaskRunDto taskRunDto) {
        int cudRslt = commonDao.update("preprocess.mergeTbTaskRun", taskRunDto);
        commonDao.insert("preprocess.insertTbTaskRunHstByPk", taskRunDto);
        return cudRslt;
    }


    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
    public int updateTbTaskRun(TaskRunDto taskRunDto) {
        int cudRslt = commonDao.update("preprocess.updateTbTaskRun", taskRunDto);
        commonDao.insert("preprocess.insertTbTaskRunHstByPk", taskRunDto);
        return cudRslt;
    }

}
