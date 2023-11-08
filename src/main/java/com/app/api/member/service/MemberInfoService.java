package com.app.api.member.service;

import com.app.api.member.dto.MemberInfoResponseDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

  private final MemberService memberService;

  public MemberInfoResponseDto getMemberInfo(final Long memberId) {
    final Member member = memberService.findMemberById(memberId);
    return MemberInfoResponseDto.of(member);
  }
}
