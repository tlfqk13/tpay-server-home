package com.tpay.commons.exception;

import org.springframework.http.HttpStatus;

public enum ErrorStatus {
  ALREADY_EXISTS(409, HttpStatus.CONFLICT, "Already Exists");

  private final int value;
  private final HttpStatus status;
  private final String message;

  ErrorStatus(final int value, final HttpStatus status, final String message) {
    this.value = value;
    this.status = status;
    this.message = message;
  }

  public int getValue() {
    return this.value;
  }

  public HttpStatus getStatus() {
    return this.status;
  }

  public String getMessage() {
    return this.message;
  }
}
