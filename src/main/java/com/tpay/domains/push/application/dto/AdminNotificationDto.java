package com.tpay.domains.push.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AdminNotificationDto {

    @Getter
    public static class Request {
        private String title;
        private String body;
    }

    @Builder
    @AllArgsConstructor
    public static class Response {
        private String message;
    }
}
