package com.tpay.commons.exception;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {
  private final ErrorStatus errorStatus;

  public AlreadyExistsException(ErrorStatus errorStatus) {
    super(errorStatus.getMessage());
    this.errorStatus = errorStatus;
  }
}
