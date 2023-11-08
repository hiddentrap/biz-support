package com.app.global.jwt.service;

import com.app.domain.member.constant.Role;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.GrantType;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.dto.JwtTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TokenManager {

  private final String accessTokenExpirationTime;
  private final String refreshTokenExpirationTime;
  private final String tokenSecret;

  public JwtTokenDto createJwtTokenDto(Long memberId, Role role) {
    final Date accessTokenExpireTime = createAccessTokenExpireTime();
    final Date refreshTokenExpireTime = createRefreshTokenExpireTime();

    final String accessToken = createAccessToken(memberId, role, accessTokenExpireTime);
    final String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);
    return JwtTokenDto.builder()
                      .grantType(GrantType.BEARER.getType())
                      .accessToken(accessToken)
                      .accessTokenExpireTime(accessTokenExpireTime)
                      .refreshToken(refreshToken)
                      .refreshTokenExpireTime(refreshTokenExpireTime)
                      .build();
  }

  public Date createAccessTokenExpireTime() {
    return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
  }

  public Date createRefreshTokenExpireTime() {
    return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
  }

  public String createAccessToken(Long memberid, Role role, Date expirationTime) {
    final String accessToken = Jwts.builder()
                                   .setSubject(TokenType.ACCESS.name())    // 토큰 제목
                                   .setIssuedAt(new Date())                // 토큰 발급시간
                                   .setExpiration(expirationTime)          // 토큰 만료시간
                                   .claim("memberId", memberid)      // 회원 아이디
                                   .claim("role", role)              // 유저 role
                                   .signWith(Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8)),
                                       SignatureAlgorithm.HS256)
                                   .setHeaderParam("typ", "JWT")
                                   .compact();
    return accessToken;
  }

  public String createRefreshToken(Long memberid, Date expirationTime) {
    final String refreshToken = Jwts.builder()
                                    .setSubject(TokenType.REFRESH.name())   // 토큰 제목
                                    .setIssuedAt(new Date())                // 토큰 발급시간
                                    .setExpiration(expirationTime)          // 토큰 만료시간
                                    .claim("memberId", memberid)      // 회원 아이디
                                    .signWith(Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8)),
                                        SignatureAlgorithm.HS256)
                                    .setHeaderParam("typ", "JWT")
                                    .compact();
    return refreshToken;
  }

  public void validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
          .build()
          .parseClaimsJws(token);

    } catch (ExpiredJwtException ex) {
      log.info("token 만료", ex);
      throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
    } catch (Exception ex) {
      log.info("요휴하지 않은 token", ex);
      throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
    }
  }

  public Claims getTokenClaims(String token) {
    Claims claims;
    try {
      claims = Jwts.parserBuilder()
                   .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8)))
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    } catch (Exception ex) {
      log.info("유효하지 않은 token", ex);
      throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
    }
    return claims;
  }
}
