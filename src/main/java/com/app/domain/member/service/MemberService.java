package com.app.domain.member.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.mapper.MemberMapper;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberMapper memberMapper;

  public Member registerMember(Member member) {
    validateDuplicateMember(member);
    memberMapper.insert(member);
    return memberMapper.selectById(member.getMemberId());
  }

  private void validateDuplicateMember(Member member) {
    final Member findMember = memberMapper.selectOne(
        new QueryWrapper<Member>().lambda().eq(Member::getEmail, member.getEmail()));
    if (findMember != null) {
      throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
    }
  }

  public Member findMemberByEmail(String email) {
    return memberMapper.selectOne(
        new QueryWrapper<Member>().lambda().eq(Member::getEmail, email));
  }

  public void updateMember(final Member member) {
    memberMapper.updateById(member);
  }

  public Member findMemberByRefreshToken(final String refreshToken) {
    final Member member = memberMapper.selectOne(
        new QueryWrapper<Member>().lambda().eq(Member::getRefreshToken, refreshToken));
    if (member == null) {
      throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
    final LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();
    if (tokenExpirationTime.isBefore(LocalDateTime.now())) {
      throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }
    return member;
  }

  public Member findMemberById(final Long memberId) {
    final Member member = memberMapper.selectById(memberId);
    if (member == null) {
      throw new EntityNotFoundException(ErrorCode.MEMBER_NOT_EXISTS);
    }
    return member;
  }
}
