package com.app.global.interceptor;

import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.service.TokenManager;
import com.app.global.util.AuthorizationHeaderUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

  private final TokenManager tokenManager;

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
      throws Exception {
    //1. Authorization Header 검증
    final String authorizationHeader = request.getHeader("Authorization");
    AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

    //2. 토큰 검증
    final String token = authorizationHeader.split(" ")[1];
    tokenManager.validateToken(token);

    //3. 토큰 타입
    final Claims claims = tokenManager.getTokenClaims(token);
    final String tokenType = claims.getSubject();
    if (!TokenType.isAccessToken(tokenType)) {
      throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
    }
    return true;
  }
}
