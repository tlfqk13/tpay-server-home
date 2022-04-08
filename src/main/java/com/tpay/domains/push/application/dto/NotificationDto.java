package com.tpay.domains.push.application.dto;

import com.tpay.commons.util.PushType;
import lombok.*;

import java.util.List;

public class NotificationDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        String title;
        String body;
        PushType pushType;
        String pushTypeValue;
        String pushCategory;
        String link;
    }
}
