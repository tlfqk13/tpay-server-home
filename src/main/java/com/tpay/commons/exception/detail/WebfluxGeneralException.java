package com.tpay.commons.exception.detail;


import com.tpay.commons.exception.CustomException;
import com.tpay.commons.exception.ExceptionState;

public class WebfluxGeneralException extends RuntimeException implements CustomException {
    private final ExceptionState state;

    public WebfluxGeneralException(ExceptionState state, String message) {
        super(message);
        this.state = state;
    }

    @Override
    public ExceptionState getState() {
        return state;
    }
}
