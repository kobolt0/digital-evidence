package kr.go.spo.common;

import kr.go.spo.dto.TaskRunDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonDao {
    private final SqlSessionTemplate sqlSessionTemplate;


    public int 	insert(String statement, Object parameter){
        log.debug("##@# sql insert. {}. {}", statement, parameter);
        int cudRslt = sqlSessionTemplate.insert(statement, parameter);
        log.debug("##@# cudRslt.{}", cudRslt);
        return cudRslt;
    }

    public int 	update(String statement, Object parameter){
        log.debug("##@# sql update. {}. {}", statement, parameter);
        int cudRslt = sqlSessionTemplate.update(statement, parameter);
        log.debug("##@# cudRslt.{}", cudRslt);
        return cudRslt;
    }

    public <E> List<E> selectList(String statement, Object parameter) {
        log.debug("##@# sql selectList. {}. {}", statement, parameter);
        List<E> list = sqlSessionTemplate.selectList(statement, parameter);
        log.debug("##@# selectList size.{}", list.size());
        return list;
    }

    public <T> T selectOne(String statement, Object parameter) {
        log.debug("##@# sql selectOne. {}. {}", statement, parameter);
        T obj = sqlSessionTemplate.selectOne(statement, parameter);
        log.debug("##@# selectOne.{}", obj);
        return obj;
    }

}
