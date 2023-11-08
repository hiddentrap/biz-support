package com.app.api.swaggertest;

import com.app.global.resolver.memberinfo.MemberInfo;
import com.app.global.resolver.memberinfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "test", description = "서버 기능 테스트 API")
@Slf4j
@RequestMapping("/api/swagger-test")
@RestController
public class SwaggerParamTestController {

  @Tag(name = "test")
  @Operation(summary = "스웨거 어노테이션 테스트", description = "스웨거 어노테이션 테스트")
  @GetMapping("/{variable}")
  public String swaggerTest(
      @Parameter(description = "회원정보", required = false, hidden = true) @MemberInfo MemberInfoDto membeInfo,
      @Parameter(description = "쿼리", required = false, example = "query") String query,
      @Parameter(description = "패스", required = true, example = "path") @PathVariable String variable) {
    log.info("query: {}, path variable: {}", query, variable);
    return "swagger test";
  }
}
