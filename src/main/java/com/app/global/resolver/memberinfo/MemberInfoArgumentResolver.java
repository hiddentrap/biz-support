package com.app.global.resolver.memberinfo;

import com.app.domain.member.constant.Role;
import com.app.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberInfoArgumentResolver implements HandlerMethodArgumentResolver {

  private final TokenManager tokenManager;

  @Override
  public boolean supportsParameter(final MethodParameter parameter) {
    final boolean hasMemberInfoAnnotation = parameter.hasParameterAnnotation(MemberInfo.class);
    final boolean hasMemberInfoDto = MemberInfoDto.class.isAssignableFrom(parameter.getParameterType());
    return hasMemberInfoAnnotation && hasMemberInfoDto;
  }

  @Override
  public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
      final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
    final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    final String authorizationHeader = request.getHeader("Authorization");
    final String token = authorizationHeader.split(" ")[1];
    final Claims claims = tokenManager.getTokenClaims(token);
    final Long memberId = Long.valueOf((Integer) claims.get("memberId"));
    final String role = (String) claims.get("role");
    return MemberInfoDto.builder()
                        .memberId(memberId)
                        .role(Role.from(role))
                        .build();
  }
}
