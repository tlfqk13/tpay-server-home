package com.tpay.commons.exception.detail;

import com.tpay.commons.exception.CustomException;
import com.tpay.commons.exception.ExceptionState;
import lombok.Getter;

@Getter
public class InvalidNationException extends RuntimeException implements CustomException {
    private final ExceptionState state;

    public InvalidNationException(ExceptionState state) {
        super(state.getMessage());
        this.state = state;
    }

    public InvalidNationException(ExceptionState state, String message) {
        super(message);
        this.state = state;
    }
}
