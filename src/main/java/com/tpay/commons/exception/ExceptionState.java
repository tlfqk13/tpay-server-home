package com.tpay.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionState {
  UNKNOWN(400, HttpStatus.BAD_REQUEST, "Unknown", "Contact Backend Developer"),
  ALREADY_EXISTS(409, HttpStatus.CONFLICT, "R0001", "Already Exists");

  private final int value;
  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
