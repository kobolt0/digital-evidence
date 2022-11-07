package gov.spo.ndfaas.bigdata.workflow.service;

import gov.spo.ndfaas.bigdata.workflow.dto.ProcessInstanceDto;
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
public class ProcessInstanceService {
    private final SqlSessionTemplate sqlSessionTemplate;


    public int insertTbProcessInstance(ProcessInstanceDto processInstanceDto) {
        int cudRslt = sqlSessionTemplate.update("preprocess.insertTbProcessInstance", processInstanceDto);
        log.debug("##@# cudRslt.{}", cudRslt);
        return cudRslt;
    }


    public int updateTbProcessInstance(ProcessInstanceDto processInstanceDto) {
        int cudRslt = sqlSessionTemplate.update("preprocess.updateTbProcessInstance", processInstanceDto);
        log.debug("##@# cudRslt.{}", cudRslt);
        return cudRslt;
    }


}
