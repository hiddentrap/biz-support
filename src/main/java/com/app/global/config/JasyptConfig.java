package com.app.global.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

  @Value("${jasypt.encryptor.password}")
  private String password;

  @Bean
  public PooledPBEStringEncryptor jasyptStringEncryptor() {
    final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setPoolSize(4); // core수와 동일하게 설정 권장
    encryptor.setPassword(password);
    encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
    return encryptor;
  }
}
