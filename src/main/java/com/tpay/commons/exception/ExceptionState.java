package com.tpay.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 예외 종류 enum 클래스
 * 하나의 예외는 에러코드, status, 내부 에러 code, 디폴트메시지를 속성으로 갖는다.
 */
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
    MISMATCH_TOKEN(401, HttpStatus.UNAUTHORIZED, "A0004", "Authentication info not matched"),
    REFUND(400, HttpStatus.BAD_REQUEST, "R0001", "Refund Exception"),
    INVALID_PASSPORT_INFO(400, HttpStatus.BAD_REQUEST, "E0001", "Contact Backend Developer"),
    INVALID_EXTERNAL_REFUND_INDEX(400,HttpStatus.BAD_REQUEST,"G0001","Invalid External Refund Index"),
    ALREADY_CANCELLED(409, HttpStatus.BAD_REQUEST, "G0002", "Already Cancelled Index"),
    WEBFLUX_GENERAL(400,HttpStatus.BAD_REQUEST, "G0003","Webflux General Exception");

    private final int value;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
