package kr.go.spo.rest;

import kr.go.spo.model.BoardPro;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    Map map = sqlSessionTemplate.selectOne("test.selectTest");
    log.debug("sql resutl:{}" , map);

    List list = sqlSessionTemplate.selectList("test.selectTest");
    log.debug("sql list resutl:{}" , list);

    return map.toString();
  }

  @GetMapping("/testList")
  public String testJsonList() {
    Map map = sqlSessionTemplate.selectOne("test.selectTest");
    log.debug("sql resutl:{}" , map);

    List list = sqlSessionTemplate.selectList("test.selectTest");
    log.debug("sql list resutl:{}" , list);

    return list.toString();
  }
  @GetMapping("/dummy")
  public Map<String, String> testDummy() {
    Map map = sqlSessionTemplate.selectOne("test.dummy");
    return map;
  }


}