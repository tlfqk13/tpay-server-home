package com.tpay.domains.push.application.dto;

import com.tpay.commons.push.detail.PushTopic;
import lombok.*;

import java.util.List;

public class TopicDto {
    @Getter
    public static class Request {
        private List<String> tokens;
        private PushTopic pushTopic;
    }
}
