package com.tpay.commons.exception.detail;

import com.tpay.commons.exception.CustomException;
import com.tpay.commons.exception.ExceptionState;
import lombok.Getter;

@Getter
public class InvalidPassportInfoException extends RuntimeException implements CustomException {
  private final ExceptionState state;
  public InvalidPassportInfoException(ExceptionState state, String message) {
    super(message);
    this.state = state;
  }
}
