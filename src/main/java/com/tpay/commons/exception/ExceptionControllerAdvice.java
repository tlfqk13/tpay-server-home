package com.tpay.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseMessage> handleIllegalArgumentException(
      IllegalArgumentException illegalArgumentException) {

    illegalArgumentException.printStackTrace();
    return ResponseEntity.badRequest()
        .body(
            ErrorResponseMessage.builder()
                .value(HttpStatus.BAD_REQUEST.value())
                .message(illegalArgumentException.getMessage())
                .build());
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponseMessage> handleGuestNotSupportedException(
      AlreadyExistsException alreadyExistsException) {

    ErrorStatus errorStatus = alreadyExistsException.getErrorStatus();
    return ResponseEntity.status(errorStatus.getStatus())
        .body(
            ErrorResponseMessage.builder()
                .value(errorStatus.getValue())
                .message(errorStatus.getMessage())
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseMessage> handleException(Exception exception) {

    exception.printStackTrace();
    return new ResponseEntity<>(
        ErrorResponseMessage.builder()
            .value(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message(
                "If you see this message, you should contact the backend developer immediately")
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
