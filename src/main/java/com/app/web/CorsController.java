package com.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CorsController {

  /**
   * CORS 설정 테스트용 컨트롤러, App을 8082 포트로 실행한 뒤, 호출시 8080 포트로 CORS 요청
   *
   * @return
   */
  @GetMapping("/cors")
  public String cors() {
    return "cors";
  }
}
