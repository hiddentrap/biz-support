package com.app.global.config.web.kakaotoken.controller;

import com.app.global.config.web.kakaotoken.client.KakaoTokenClient;
import com.app.global.config.web.kakaotoken.dto.KakaoTokenDto.Request;
import com.app.global.config.web.kakaotoken.dto.KakaoTokenDto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

  private final KakaoTokenClient kakaoTokenClient;
  @Value("${kakao.client.id}")
  private String clientId;
  @Value("${kakao.client.secret}")
  private String clientSecret;

  @GetMapping("/login")
  public String login() {
    return "loginForm";
  }

  @GetMapping("/oauth/kakao/callback")
  public @ResponseBody String loginCallback(String code) {
    String contentType = "application/x-www-form-urlencoded;charset=utf-8";
    final Request kakaoTokenRequestDto = Request.builder()
                                                .client_id(clientId)
                                                .client_secret(clientSecret)
                                                .grant_type("authorization_code")
                                                .code(code)
                                                .redirect_uri("http://localhost:8080/oauth/kakao/callback")
                                                .build();
    final Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
    return "kakao token : " + kakaoToken;
  }
}
