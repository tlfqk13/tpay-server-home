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
    ID_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "B0002", "Cannot find by Id"),
    ALREADY_EXISTS(409, HttpStatus.CONFLICT, "R0001", "Already Exists"),
    CHECK_ITEM_PRICE(400, HttpStatus.BAD_REQUEST, "R0002", "Check Item Price Over 30,000"),
    AUTHENTICATION_FAILED(401, HttpStatus.UNAUTHORIZED, "A0001", "Invalid Authentication"),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "A0002", "Invalid Auth Token"),
    FORCE_REFRESH(401, HttpStatus.UNAUTHORIZED, "A0003", "Sign-in Again"),
    MISMATCH_TOKEN(401, HttpStatus.UNAUTHORIZED, "A0004", "Authentication info not matched"),
    DUPLICATE_SIGNIN(400, HttpStatus.BAD_REQUEST, "A0005", "Duplicate sign-in"),
    DUPLICATE_SIGNOUT(400, HttpStatus.UNAUTHORIZED, "A0006", "Duplicate sign-out force"),
    REFUND(400, HttpStatus.BAD_REQUEST, "R0001", "Refund Exception"),
    ORDER_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "R0002", "Order Not Found, create Order First"),
    INVALID_PASSPORT_INFO(400, HttpStatus.BAD_REQUEST, "E0001", "Contact Backend Developer"),
    INVALID_EXTERNAL_REFUND_INDEX(400,HttpStatus.BAD_REQUEST,"G0001","Invalid External Refund Index"),
    ALREADY_CANCELLED(409, HttpStatus.BAD_REQUEST, "G0002", "Already Cancelled Index"),
    INVALID_NATION(400,HttpStatus.BAD_REQUEST, "G0004","Invalid Passport Nation"),
    CUSTOMER_NOT_FOUND(400,HttpStatus.BAD_REQUEST,"C0001","Customer not Found"),
    WEBFLUX_GENERAL(400,HttpStatus.BAD_REQUEST, "G0003","Webflux General Exception"),
    ;

    private final int value;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
