package com.tpay.commons.exception.detail;

import com.tpay.commons.exception.CustomException;
import com.tpay.commons.exception.ExceptionState;
import lombok.Getter;

@Getter
public class FranchiseeAuthenticationException extends RuntimeException implements CustomException {
    private final ExceptionState state;

    public FranchiseeAuthenticationException(ExceptionState state) {
        super(state.getMessage());
        this.state = state;
    }

    public FranchiseeAuthenticationException(ExceptionState state, String message) {
        super(message);
        this.state = state;
    }
}
