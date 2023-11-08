package com.app.api.exceptiontest.controller;

import com.app.api.exceptiontest.dto.BindExceptionTestDto;
import com.app.api.exceptiontest.dto.TestEnum;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "test", description = "서버 기능 테스트 API")
@RestController
@RequestMapping("/api/exception")
public class ExceptionTestController {

  @Tag(name = "test")
  @Operation(summary = "Bind 예외 테스트", description = "Bind 예외 테스트")
  @GetMapping("/bind-exception-test")
  public String bindExceptionTest(@Valid BindExceptionTestDto bindExceptionTestDto) {
    return "ok";
  }

  @Tag(name = "test")
  @Operation(summary = "TypeMismatch 예외 테스트", description = "TypeMismatch 예외 테스트")
  @GetMapping("/type-exception-test")
  public String typeMismatchException(TestEnum testEnum) {
    return "ok";
  }

  @Tag(name = "test")
  @Operation(summary = "비지니스 예외 테스트", description = "비지니스 예외 테스트")
  @GetMapping("/business-exception-test")
  public String businessExceptionTest(String isError) {
    if ("true".equals(isError)) {
      throw new BusinessException(ErrorCode.TEST);
    }
    return "ok";
  }

  @Tag(name = "test")
  @Operation(summary = "그 외 시스템 예외 테스트", description = "그 외 시스템 예외 테스트")
  @GetMapping("/exception-test")
  public String exceptionTest(String isError) {
    if ("true".equals(isError)) {
      throw new IllegalArgumentException("예외 테스트");
    }
    return "ok";
  }
}
