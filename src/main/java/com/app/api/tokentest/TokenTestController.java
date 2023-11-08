package com.app.api.tokentest;

import com.app.domain.member.constant.Role;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "test", description = "서버 기능 테스트 API")
@Slf4j
@RestController
@RequestMapping("/api/token-test")
@RequiredArgsConstructor
public class TokenTestController {

  private final TokenManager tokenManager;

  @Tag(name = "test")
  @Operation(summary = "JWT 토큰 생성 테스트 API", description = "JWT 토큰 생성 테스트 API")
  @GetMapping("/create")
  public JwtTokenDto createJwtTokenDto() {
    return tokenManager.createJwtTokenDto(1L, Role.ADMIN);
  }

  @Tag(name = "test")
  @Operation(summary = "JWT 토큰 유효성 검사 테스트 API", description = "JWT 토큰에 대한 유효성 검사")
  @GetMapping("/valid")
  public String validateJwtToken(@RequestParam String token) {
    tokenManager.validateToken(token);
    final Claims claims = tokenManager.getTokenClaims(token);
    final Long memberId = Long.valueOf((Integer) claims.get("memberId"));
    final String role = (String) claims.get("role");
    log.info("memberId : {}", memberId);
    log.info("role : {}", role);
    return "success";
  }
}
