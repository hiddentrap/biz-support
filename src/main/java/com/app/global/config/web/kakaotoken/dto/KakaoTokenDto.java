package com.app.global.config.web.kakaotoken.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class KakaoTokenDto {

  @Builder
  @Getter
  public static class Request {

    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String code;
    private String client_secret;
  }

  @ToString
  @Getter
  @NoArgsConstructor
  public static class Response {

    private String token_type;
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;
    private String scope;

    @Builder
    public Response(final String token_type, final String access_token, final Integer expires_in,
        final String refresh_token,
        final Integer refresh_token_expires_in, final String scope) {
      this.token_type = token_type;
      this.access_token = access_token;
      this.expires_in = expires_in;
      this.refresh_token = refresh_token;
      this.refresh_token_expires_in = refresh_token_expires_in;
      this.scope = scope;
    }
  }

}
