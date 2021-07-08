package com.tpay.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionState {
  UNKNOWN(400, HttpStatus.BAD_REQUEST, "Unknown", "Contact Backend Developer"),
  INVALID_PASSWORD(400, HttpStatus.BAD_REQUEST, "P0001", "Invalid Password"),
  INVALID_BUSINESS_NUMBER(400, HttpStatus.BAD_REQUEST, "B0001", "Invalid Business Number"),
  ALREADY_EXISTS(409, HttpStatus.CONFLICT, "R0001", "Already Exists");

  private final int value;
  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
