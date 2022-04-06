package com.tpay.domains.push.test.application.dto;

import com.tpay.commons.util.PushType;
import lombok.*;

public class NotificationDto {

    @Getter
    public static class Request {
        String title;
        String body;
        PushType type;
        String typeValue;
        String num;
        String linking;
    }
}
