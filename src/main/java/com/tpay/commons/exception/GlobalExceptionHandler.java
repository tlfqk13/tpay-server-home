package com.tpay.commons.exception;

import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.commons.exception.detail.UnknownException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(
      HttpServletRequest request, AlreadyExistsException alreadyExistsException) {

    System.out.println("handleAlreadyExistsException() : " + alreadyExistsException.getMessage());
    ExceptionResponse response = ExceptionResponse.of(request, alreadyExistsException);

    return ResponseEntity.status(response.getValue()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleException(
      HttpServletRequest request, Exception exception) {

    exception.printStackTrace();
    ExceptionResponse response =
        ExceptionResponse.of(request, new UnknownException(ExceptionState.UNKNOWN));

    return ResponseEntity.badRequest().body(response);
  }
}
