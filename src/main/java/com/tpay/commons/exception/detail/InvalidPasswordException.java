package com.tpay.commons.exception.detail;

import com.tpay.commons.exception.CustomException;
import com.tpay.commons.exception.ExceptionState;
import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException implements CustomException {
    private final ExceptionState state;

    public InvalidPasswordException(ExceptionState state) {
        super(state.getMessage());
        this.state = state;
    }

    public InvalidPasswordException(ExceptionState state, String message) {
        super(message);
        this.state = state;
    }
}
