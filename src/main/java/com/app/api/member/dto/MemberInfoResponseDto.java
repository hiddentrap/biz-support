package com.app.api.member.dto;

import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {

  @Schema(description = "회원 아이디", example = "1", requiredMode = RequiredMode.REQUIRED)
  private Long memberId;
  @Schema(description = "이메일", example = "test@gmail.com", requiredMode = RequiredMode.REQUIRED)
  private String email;
  @Schema(description = "회원 이름", example = "홍길동", requiredMode = RequiredMode.REQUIRED)
  private String memberName;
  @Schema(description = "프로필 이미지 경로", example = "http://k.kakaocdn.net/img_1010x110.jpg", requiredMode = RequiredMode.NOT_REQUIRED)
  private String profile;
  @Schema(description = "회원의 역할", example = "USER", requiredMode = RequiredMode.REQUIRED)
  private Role role;

  public static MemberInfoResponseDto of(final Member member) {
    return MemberInfoResponseDto.builder()
                                .memberId(member.getMemberId())
                                .memberName(member.getMemberName())
                                .email(member.getEmail())
                                .profile(member.getProfile())
                                .role(member.getRole())
                                .build();
  }
}
