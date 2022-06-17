package kr.go.spo.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class TestRest {
  private SqlSessionTemplate sqlSessionTemplate;

  @GetMapping("/testJson")
  public String testJsonObj() {
    Map<String, String> map = sqlSessionTemplate.selectOne("test.selectTest");
    log.debug("##@# sql resutl:{}" , map);

    List<Map<String, String>> list = sqlSessionTemplate.selectList("test.selectTest");
    log.debug("##@# sql list resutl:{}" , list);

    return map.toString();
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
  public Map<String, String> testDummy() throws InterruptedException {
    Map<String, String> map = new HashMap<>();

    List <Map<String, String>> list = sqlSessionTemplate.selectList("test.dummy");
    log.debug("#@## sql result:{}");
    for (Map<String, String> tmpMap: list) {
      if (tmpMap.containsKey("col1")){
        map.put(tmpMap.get("col1"), tmpMap.get("col2"));
      }
      else{
        // h2 DB는 조회값 키가 대문자로 나옴.
        map.put(tmpMap.get("COL1"), tmpMap.get("COL2"));
      }
    }

    Thread.sleep(1000);

    return map;
  }


}