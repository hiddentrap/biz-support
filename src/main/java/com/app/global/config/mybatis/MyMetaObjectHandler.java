package com.app.global.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class MyMetaObjectHandler implements MetaObjectHandler {

  private final HttpServletRequest httpServletRequest;

  @Override
  public void insertFill(final MetaObject metaObject) {
    this.strictInsertFill(metaObject, "createdBy", String.class, getCurrentUser());
    this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
  }

  @Override
  public void updateFill(final MetaObject metaObject) {
    metaObject.setValue("modifiedBy", getCurrentUser());
    metaObject.setValue("updateTime", LocalDateTime.now());
  }

  private String getCurrentUser() {
    String modifiedBy = httpServletRequest.getRequestURI();
    if (!StringUtils.hasText(modifiedBy)) {
      modifiedBy = "unknown";
    }
    return modifiedBy;
  }
}
