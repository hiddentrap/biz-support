package com.app.global.jwt.constant;

import lombok.Getter;

@Getter
public enum GrantType {
  BEARER("Bearer");

  private String type;

  GrantType(final String type) {
    this.type = type;
  }
}
