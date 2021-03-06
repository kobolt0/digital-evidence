package kr.go.spo.controller;

import kr.go.spo.dto.TaskRunDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST 컨트롤로 테스트
 */
@Slf4j
@RestController
@AllArgsConstructor
public class TestRest {
  private SqlSessionTemplate sqlSessionTemplate;
  @GetMapping("/testJson")
  public String testJsonObj() {
    Map<String, String> map = sqlSessionTemplate.selectOne("test.selectTest");
    log.debug("##@# sql resutl:{}" , map);


    return map.toString();
  }
  @GetMapping("/test2")
  public String testJsonObj2() {

    TaskRunDto obj = sqlSessionTemplate.selectOne("test.selectTest2");
    log.debug("##@# sql resutl:{}" , obj);
    return obj.toString();
  }

  @GetMapping("/testList")
  public String testJsonList() {
    Map<String, String> map = sqlSessionTemplate.selectOne("test.selectTest");
    log.debug("##@# sql resutl:{}" , map);

    List<Map<String, String>> list = sqlSessionTemplate.selectList("test.selectTest");
    log.debug("##@# sql list resutl:{}" , list);

    return list.toString();
  }
  @GetMapping("/dummy")
  public Map<String, String> testDummy(HttpServletRequest req) throws InterruptedException {
    log.debug("##@# REST. {}", req.getRequestURL());
    Map<String, String> map = new HashMap<>();

    List <Map<String, String>> list = sqlSessionTemplate.selectList("test.dummy");
    log.debug("#@## sql result:{}", list);
    for (Map<String, String> tmpMap: list) {
      map.put(tmpMap.get("NAME"), tmpMap.get("VAL"));
    }
    int sleepTime = 1000 * Integer.valueOf(map.get("sleepTime"));
    Thread.sleep(sleepTime);
    log.debug("##@# sleep {} sec",sleepTime);
    return map;
  }


}