package com.tpay.commons.exception;

public interface CustomException {
  ExceptionState getState();

  String getMessage();
}
