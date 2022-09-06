package com.tpay.domains.certifications.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TokenResponse {
    private Long code;
    private String message;
    private Response response;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @ToString
    public static class Response {
        private String access_token;
        private Long now;
        private Long expired_at;
    }
}
