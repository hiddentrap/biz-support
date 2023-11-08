package com.app.api.health.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HealthCheckResponseDto {

  @Schema(description = "서버 health 상태", example = "ok", requiredMode = RequiredMode.REQUIRED)
  private String health;
  @ArraySchema(schema = @Schema(description = "현재 실행 중인 profile", example = "[dev]", requiredMode = RequiredMode.REQUIRED))
  private List<String> activeProfiles;

  @Builder
  public HealthCheckResponseDto(final String health, final List<String> activeProfiles) {
    this.health = health;
    this.activeProfiles = activeProfiles;
  }
}
