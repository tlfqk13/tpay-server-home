package com.tpay.commons.exception;

import com.tpay.commons.exception.detail.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception 발생시 각 로직 수행하는 RestControllerAdvice 클래스
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParameterException(
        HttpServletRequest request, InvalidParameterException exception) {

        log.error("handleInvalidParameterException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(JwtRuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleJwtRuntimeException(
        HttpServletRequest request, JwtRuntimeException exception) {

        log.error("handleJwtRuntimeException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(FranchiseeAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleFranchiseeAuthenticationException(
        HttpServletRequest request, FranchiseeAuthenticationException exception) {

        log.error("handleFranchiseeAuthenticationException() : {}" , exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(
        HttpServletRequest request, AlreadyExistsException exception) {

        log.error("handleAlreadyExistsException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(
        HttpServletRequest request, InvalidPasswordException exception) {

        log.error("handleInvalidPasswordException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(InvalidBusinessNumberException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidBusinessNumberException(
        HttpServletRequest request, InvalidBusinessNumberException exception) {

        log.error("handleInvalidBusinessNumberException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);

        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(InvalidPassportInfoException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPassportInfoException(
        HttpServletRequest request, InvalidPassportInfoException exception) {
        log.error("handleInvalidPassportInfoException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);
        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(InvalidExternalRefundIndexException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidExternalRefundIndexException(
        HttpServletRequest request, InvalidExternalRefundIndexException exception){
        log.error("handleInvalidExternalRefundIndexException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);
        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(WebfluxGeneralException.class)
    public ResponseEntity<ExceptionResponse> handleWebfluxGeneralException(
        HttpServletRequest request, WebfluxGeneralException exception) {
        log.error("handleWebfluxGeneralException() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);
        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ExceptionResponse> orderNotFoundExHandler(
            HttpServletRequest request, OrderNotFoundException exception) {
        log.error("orderNotFound Exception() : {}", exception.getMessage());
        ExceptionResponse response = ExceptionResponse.of(request, exception);
        return ResponseEntity.status(response.getValue()).body(response);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ExceptionResponse> updateFailureHandler(
            HttpServletRequest request, WebfluxGeneralException exception) {
        log.error("OptimisticLockingFailureException() : {}", "OptimisticLockingFailureException, Update Failed");
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
