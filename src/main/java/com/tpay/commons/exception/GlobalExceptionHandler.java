package com.tpay.commons.exception;

import com.tpay.commons.exception.detail.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParameterException(
        HttpServletRequest request, InvalidParameterException exception) {

        System.out.println("handleInvalidParameterException() : " + exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(JwtRuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleJwtRuntimeException(
        HttpServletRequest request, JwtRuntimeException exception) {

        System.out.println("handleJwtRuntimeException() : " + exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(FranchiseeAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleFranchiseeAuthenticationException(
        HttpServletRequest request, FranchiseeAuthenticationException exception) {

        System.out.println("handleFranchiseeAuthenticationException() : " + exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

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

    @ExceptionHandler(InvalidPassportInfoException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPassportInfoException(
        HttpServletRequest request, InvalidPassportInfoException exception) {
        System.out.println("handleInvalidPassportInfoException() : " + exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);
        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(InvalidExternalRefundIndexException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidExternalRefundIndexException(
        HttpServletRequest request, InvalidExternalRefundIndexException exception){
        System.out.println("handleInvalidExternalRefundIndexException() : "+ exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);
        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(WebfluxGeneralException.class)
    public ResponseEntity<ExceptionResponse> handleWebfluxGeneralException(
        HttpServletRequest request, WebfluxGeneralException exception) {
        System.out.println("handleWebfluxGeneralException() : "+ exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);
        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(
        HttpServletRequest request, Exception exception) {

        exception.printStackTrace();
        ExceptionResponse response =
            ExceptionResponse.of(request, new UnknownException(ExceptionState.UNKNOWN, exception.getMessage()));

        return ResponseEntity.badRequest().body(response);
    }


}
