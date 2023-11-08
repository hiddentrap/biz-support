package com.app.global.resolver.memberinfo;

import com.app.domain.member.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {

  private Long memberId;
  private Role role;
}
