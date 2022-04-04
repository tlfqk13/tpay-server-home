package com.tpay.commons.push;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class PushMessageBuilder {

    private boolean validate_only;
    private Message message;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Message {
        private Map<String,String> data;
        private Notification notification;
        private String token;
        private Apns apns;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Apns{
        private Payload payload;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Payload{
        private Aps aps;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Aps{
        private String sound;
    }
}
