package com.app.global.interceptor;

import com.app.domain.member.constant.Role;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

  private final TokenManager tokenManager;

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
      throws Exception {

    final String authorizationHeader = request.getHeader("Authorization");
    final String accessToken = authorizationHeader.split(" ")[1];
    // 인증 인터셉터에서 검증된 토큰이므로 유효성 검사는 하지 않음
    final Claims claims = tokenManager.getTokenClaims(accessToken);
    final String role = (String) claims.get("role");
    if (!Role.ADMIN.equals(Role.valueOf(role))) {
      throw new AuthenticationException(ErrorCode.FORBIDDEN_ADMIN);
    }

    return true;
  }
}
