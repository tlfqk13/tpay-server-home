package com.tpay.commons.exception.detail;

import lombok.Getter;

@Getter
public class KtpApiException extends RuntimeException {

    public KtpApiException(String code, String message) {
        super(code + ":" + message);
    }
}
