package com.tpay.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 예외 종류 enum 클래스
 * 하나의 예외는 에러코드, status, 내부 에러 code, 디폴트메시지를 속성으로 갖는다.
 * Code
 * P : 계정 관련
 * A : 인증 관련
 * G : 이외 글로벌 에러
 */
@Getter
@AllArgsConstructor
public enum ExceptionState {
    /**
     * P - 도메인 관련 에러
     */
    INVALID_PASSPORT_INFO(400, HttpStatus.BAD_REQUEST, "P0001", "Contact Backend Developer"),
    INVALID_PASSWORD(400, HttpStatus.BAD_REQUEST, "P0002", "Invalid Password"),
    INVALID_BUSINESS_NUMBER(400, HttpStatus.BAD_REQUEST, "P0003", "Invalid Business Number"),
    INVALID_NATION(400,HttpStatus.BAD_REQUEST, "P0004","Invalid Passport Nation"),
    ID_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "P0005", "Cannot find by Id"),
    CUSTOMER_NOT_FOUND(400,HttpStatus.BAD_REQUEST,"P0006","Customer not Found"),
    ALREADY_EXISTS(409, HttpStatus.CONFLICT, "P0007", "Already Exists"),
    ALREADY_CANCELLED(409, HttpStatus.BAD_REQUEST, "P0008", "Already Cancelled Index"),

    /**
     * A - 인증 관련
     */
    AUTHENTICATION_FAILED(401, HttpStatus.UNAUTHORIZED, "A0001", "Invalid Authentication"),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "A0002", "Invalid Auth Token"),
    FORCE_REFRESH(401, HttpStatus.UNAUTHORIZED, "A0003", "Sign-in Again"),
    MISMATCH_TOKEN(401, HttpStatus.UNAUTHORIZED, "A0004", "Authentication info not matched"),
    DUPLICATE_SIGNIN(400, HttpStatus.BAD_REQUEST, "A0005", "Duplicate sign-in"),
    DUPLICATE_SIGNOUT(400, HttpStatus.UNAUTHORIZED, "A0006", "Duplicate sign-out force"),

    /**
     * R - 환급 관련
     */
    REFUND(400, HttpStatus.BAD_REQUEST, "R0001", "Refund Exception"),
    ORDER_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "R0002", "Order Not Found, create Order First"),
    KOR_CUSTOMER(400, HttpStatus.BAD_REQUEST, "R0003", "KOR Customer"),
    CHECK_ITEM_PRICE(400, HttpStatus.BAD_REQUEST, "R0004", "Check Item Price Over 30,000"),
    INVALID_EXTERNAL_REFUND_INDEX(400,HttpStatus.BAD_REQUEST,"R0005","Invalid External Refund Index"),

    /**
     * G - 글로벌 에러
     */
    UNKNOWN(400, HttpStatus.BAD_REQUEST, "G0001", "Contact Backend Developer"),
    INVALID_PARAMETER(400, HttpStatus.BAD_REQUEST, "G0002", "Invalid Parameter"),
    DUPLICATE_REQUEST(400,HttpStatus.BAD_REQUEST,"G0003","Duplicate Request"),
    WEBFLUX_GENERAL(400,HttpStatus.BAD_REQUEST, "G0004","Webflux General Exception")
    ;

    private final int value;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
