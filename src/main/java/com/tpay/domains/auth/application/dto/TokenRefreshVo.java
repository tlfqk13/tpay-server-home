package com.tpay.domains.auth.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

public class TokenRefreshVo {

    @Getter
    @NoArgsConstructor
    public static class Request {
        String refreshToken;
    }

    @Value
    public static class Response {
        String accessToken;
    }
}
