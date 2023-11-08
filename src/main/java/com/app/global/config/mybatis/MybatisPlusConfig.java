package com.app.global.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.app.domain")
public class MybatisPlusConfig {

}
