package com.app.api.login.dto;

import com.app.global.jwt.dto.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OauthLoginDto {

  @Getter
  @Setter
  public static class Request {

    @Schema(description = "소셜 로그인 회원 타입", example = "KAKAO", requiredMode = RequiredMode.REQUIRED)
    private String memberType;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    @Schema(description = "grantType", example = "Baerer", requiredMode = RequiredMode.REQUIRED)
    private String grantType;
    @Schema(description = "refresh token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2ODMxMjQ1NTEsImV4cCI6MTY4MzEyNTQ1MSwibWVtYmVySWQiOjEyLCJyb2xlIjoiVVNFUiJ9.R9K9tZ1EnTbQn3qG1_jU_JQRXuMxgi4mWhpR1wcWiPI", requiredMode = RequiredMode.REQUIRED)
    private String accessToken;
    @Schema(description = "access token 만료 시간", example = "2023-05-03 23:50:51", requiredMode = RequiredMode.REQUIRED)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date accessTokenExpireTime;
    @Schema(description = "refresh token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjgzMTI0NTUyLCJleHAiOjE2ODQzMzQxNTEsIm1lbWJlcklkIjoxMn0.d3wCM9gkWWUpgrXySVQeFpFmTgKKa4IPmajhu4FvMvo", requiredMode = RequiredMode.REQUIRED)
    private String refreshToken;
    @Schema(description = "refresh token token 만료 시간", example = "2023-05-18 23:50:51", requiredMode = RequiredMode.REQUIRED)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date refreshTokenExpireTime;

    public static Response of(JwtTokenDto jwtTokenDto) {
      return Response.builder()
                     .grantType(jwtTokenDto.getGrantType())
                     .accessToken(jwtTokenDto.getAccessToken())
                     .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
                     .refreshToken(jwtTokenDto.getRefreshToken())
                     .refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
                     .build();
    }
  }
}
