package com.tpay.commons.exception;

import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class ExceptionResponse {
  private int value;
  private String reason;
  private String code;
  private String message;
  private String requestURI;
  private String queryString;
  private String requestMethod;

  public static ExceptionResponse of(CustomException exception) {
    ExceptionState state = exception.getState();
    HttpStatus httpStatus = state.getHttpStatus();

    return ExceptionResponse.builder()
        .value(state.getValue())
        .reason(httpStatus.getReasonPhrase())
        .code(state.getCode())
        .message(exception.getMessage())
        .requestURI("")
        .queryString("")
        .requestMethod("")
        .build();
  }

  public static ExceptionResponse of(HttpServletRequest request, CustomException exception) {
    ExceptionState state = exception.getState();
    HttpStatus httpStatus = state.getHttpStatus();

    return ExceptionResponse.builder()
        .value(state.getValue())
        .reason(httpStatus.getReasonPhrase())
        .code(state.getCode())
        .message(exception.getMessage())
        .requestURI(request.getRequestURI())
        .queryString(request.getQueryString())
        .requestMethod(request.getMethod())
        .build();
  }
}
