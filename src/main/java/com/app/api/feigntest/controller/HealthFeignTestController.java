package com.app.api.feigntest.controller;

import com.app.api.feigntest.client.HelloClient;
import com.app.api.health.dto.HealthCheckResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "test", description = "서버 기능 테스트 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthFeignTestController {

  private final HelloClient helloClient;

  @Tag(name = "test")
  @Operation(summary = "feign 사용 테스트 API", description = "feign 사용 테스트 API")
  @GetMapping("/health/feign-test")
  public ResponseEntity<HealthCheckResponseDto> healthCheckTest() {
    final HealthCheckResponseDto healthCheckResponseDto = helloClient.healthCheck();
    return ResponseEntity.ok(healthCheckResponseDto);
  }
}
