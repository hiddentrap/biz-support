package com.app.api.logout.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutService {

  private final MemberService memberService;
  private final TokenManager tokenManager;

  public void logout(final String accessToken) {
    // 1. 토큰 검증
    tokenManager.validateToken(accessToken);
    // 2. 토큰 타입 확인
    final Claims claims = tokenManager.getTokenClaims(accessToken);
    final String tokenType = claims.getSubject();
    if (!TokenType.isAccessToken(tokenType)) {
      throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
    }
    // 3. refresh token 만료 처리
    final Long memberId = Long.valueOf((Integer) claims.get("memberId"));
    Member member = memberService.findMemberById(memberId);
    member.expireRefreshToken(LocalDateTime.now());
    memberService.updateMember(member);
  }
}
