package com.app.domain.member.entity;

import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.util.DateTimeUtils;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@TableName(value = "member")
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @TableId(type = IdType.AUTO)
  private Long memberId;
  private MemberType memberType;
  private String email;
  private String password;
  private String memberName;
  private String profile;
  private Role role;
  private String refreshToken;
  private LocalDateTime tokenExpirationTime;
  @TableField(fill = FieldFill.INSERT)
  private String createdBy;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String modifiedBy;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @Builder
  public Member(MemberType memberType, String email, String password, String memberName,
      String profile, Role role) {
    this.memberType = memberType;
    this.email = email;
    this.password = password;
    this.memberName = memberName;
    this.profile = profile;
    this.role = role;
  }

  public void updateRefreshToken(final JwtTokenDto jwtTokenDto) {
    this.refreshToken = jwtTokenDto.getRefreshToken();
    this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtTokenDto.getRefreshTokenExpireTime());
  }

  public void expireRefreshToken(final LocalDateTime now) {
    this.tokenExpirationTime = now;
  }
}
