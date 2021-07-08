package com.tpay.commons.exception;

import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.exception.detail.UnknownException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(
      HttpServletRequest request, AlreadyExistsException exception) {

    System.out.println("handleAlreadyExistsException() : " + exception.getMessage());
    ExceptionResponse response = ExceptionResponse.of(request, exception);

    return ResponseEntity.status(response.getValue()).body(response);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(
      HttpServletRequest request, InvalidPasswordException exception) {

    System.out.println("handleInvalidPasswordException() : " + exception.getMessage());
    ExceptionResponse response = ExceptionResponse.of(request, exception);

    return ResponseEntity.status(response.getValue()).body(response);
  }

  @ExceptionHandler(InvalidBusinessNumberException.class)
  public ResponseEntity<ExceptionResponse> handleInvalidBusinessNumberException(
      HttpServletRequest request, InvalidBusinessNumberException exception) {

    System.out.println("handleInvalidBusinessNumberException() : " + exception.getMessage());
    ExceptionResponse response = ExceptionResponse.of(request, exception);

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
