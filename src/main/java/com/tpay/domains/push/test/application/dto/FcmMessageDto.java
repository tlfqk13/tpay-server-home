package com.tpay.domains.push.test.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class FcmMessageDto {
    private boolean validate_only;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Data
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Data
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
