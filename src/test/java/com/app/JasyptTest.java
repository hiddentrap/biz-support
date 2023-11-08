package com.app;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JasyptTest {

  @Test
  @DisplayName("jasyptTest")
  void jasyptTest() {
    //given
    String password = "dummy";
    final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setPoolSize(4); // core수와 동일하게 설정 권장
    encryptor.setPassword(password);
    encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
    String content = "rabbit"; // 암호화 내용
    //when
    final String encrypted = encryptor.encrypt(content);// 암호화
    final String decrypted = encryptor.decrypt(encrypted); // 복호화
//then
    System.out.println("Enc: " + encrypted + ", Dec: " + decrypted);
  }

}
