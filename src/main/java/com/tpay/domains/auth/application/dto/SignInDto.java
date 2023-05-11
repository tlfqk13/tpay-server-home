package com.tpay.domains.auth.application.dto;

import lombok.Getter;

@Getter
public class SignInDto {

    @Getter
    public static class Request {
        private String businessNumber;
        private String userId;
        private String password;
        private String pushToken;
    }
}
