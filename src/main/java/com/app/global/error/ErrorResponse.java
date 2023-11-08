package com.app.global.error;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@Builder
public class ErrorResponse {

  private String errorCode;
  private String errorMessage;

  public static ErrorResponse of(String errorCode, String errorMessage) {
    return ErrorResponse.builder()
                        .errorCode(errorCode)
                        .errorMessage(errorMessage)
                        .build();
  }

  public static ErrorResponse of(String errorCode, BindingResult bindingResult) {
    return ErrorResponse.builder()
                        .errorCode(errorCode)
                        .errorMessage(createErrorMessage(bindingResult))
                        .build();
  }

  private static String createErrorMessage(final BindingResult bindingResult) {
    final StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    for (final FieldError fieldError : fieldErrors) {
      if (!isFirst) {
        sb.append(", ");
      } else {
        isFirst = false;
      }
      sb.append("[")
        .append(fieldError.getField())
        .append("] ")
        .append(fieldError.getDefaultMessage());
    }
    return sb.toString();
  }
}
