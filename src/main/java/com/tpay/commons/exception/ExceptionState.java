package com.tpay.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionState {
  UNKNOWN(400, HttpStatus.BAD_REQUEST, "Unknown", "Contact Backend Developer"),
  INVALID_PARAMETER(400, HttpStatus.BAD_REQUEST, "P0001", "Invalid Parameter"),
  INVALID_PASSWORD(400, HttpStatus.BAD_REQUEST, "P0002", "Invalid Password"),
  INVALID_BUSINESS_NUMBER(400, HttpStatus.BAD_REQUEST, "B0001", "Invalid Business Number"),
  ALREADY_EXISTS(409, HttpStatus.CONFLICT, "R0001", "Already Exists"),
  AUTHENTICATION_FAILED(401, HttpStatus.UNAUTHORIZED, "A0001", "Invalid Authentication"),
  INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "A0002", "Invalid Auth Token"),
  FORCE_REFRESH(401, HttpStatus.UNAUTHORIZED, "A0003", "Sign-in Again"),
  REFUND(400, HttpStatus.BAD_REQUEST, "R0001", "Refund Exception"),
  INVALID_PASSPORT_INFO(400,HttpStatus.BAD_REQUEST,"E0001","Contact Backend Developer")
  ;

  private final int value;
  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
