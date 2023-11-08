package com.app.global.config;

import com.app.global.error.FeignClientExceptionErrorDecoder;
import feign.Logger;
import feign.Logger.Level;
import feign.Retryer;
import feign.Retryer.Default;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients(basePackages = "com.app") // TODO: 2023-04-30 패키지명 수정
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

  /**
   * - NONE: 로깅안함 - BASIC: 요청메서드와 URL, 응답상태 코드, 실행시간 - HEADERS: BASIC + request와 reponse의 headers 로깅 - FULL: HEADERS +
   * body, metadata를 포함하여 모두 로깅
   *
   * @return
   */
  @Bean
  Logger.Level feignLoggerLevel() {
    return Level.FULL;
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return new FeignClientExceptionErrorDecoder();
  }

  @Bean
  public Retryer retryer() {
    return new Default(1000, 2000, 3);
  }
}
