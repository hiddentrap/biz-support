package com.app.external.oauth.model;

import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAttributes {

  private String name;
  private String email;
  private String profile;
  private MemberType memberType;

  public Member toMemberEntity(MemberType memberType, Role role) {
    return Member.builder()
                 .memberName(name)
                 .email(email)
                 .memberType(memberType)
                 .profile(profile)
                 .role(role)
                 .build();
  }
}
