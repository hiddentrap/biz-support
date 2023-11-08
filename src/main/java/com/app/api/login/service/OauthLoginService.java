package com.app.api.login.service;

import com.app.api.login.dto.OauthLoginDto;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.external.oauth.model.OAuthAttributes;
import com.app.external.oauth.service.SocialLoginApiService;
import com.app.external.oauth.service.SocialLoginApiServiceFactory;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

  private final MemberService memberService;
  private final TokenManager tokenManager;

  public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
    final SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(
        memberType);
    final OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
    log.info("userInfo : {}", userInfo);

    JwtTokenDto jwtTokenDto;
    final Member findMember = memberService.findMemberByEmail(userInfo.getEmail());
    if (findMember == null) {
      // 신규 회원 가입
      final Member oauthMember = userInfo.toMemberEntity(memberType, Role.USER);
      memberService.registerMember(oauthMember);
      // 토큰 생성
      jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
      oauthMember.updateRefreshToken(jwtTokenDto);
      memberService.updateMember(oauthMember);
    } else {
      // 기존 회원
      // 토큰 생성
      jwtTokenDto = tokenManager.createJwtTokenDto(findMember.getMemberId(), findMember.getRole());
      findMember.updateRefreshToken(jwtTokenDto);
      memberService.updateMember(findMember);
    }

    return OauthLoginDto.Response.of(jwtTokenDto);
  }
}
