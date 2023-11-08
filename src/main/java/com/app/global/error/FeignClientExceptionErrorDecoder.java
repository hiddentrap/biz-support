package com.app.global.error;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

  private ErrorDecoder errorDecoder = new Default();

  @Override
  public Exception decode(final String methodKey, final Response response) {
    log.error("{} 요청 실패, status: {}, reason: {}", methodKey, response.status(), response.reason());
    final FeignException exception = FeignException.errorStatus(methodKey, response);
    final HttpStatus httpStatus = HttpStatus.valueOf(response.status());
    if (httpStatus.is5xxServerError()) {
      return new RetryableException(
          response.status(),
          exception.getMessage(),
          response.request().httpMethod(),
          exception,
          null,
          response.request()
      );
    }
    return errorDecoder.decode(methodKey, response);
  }
}
