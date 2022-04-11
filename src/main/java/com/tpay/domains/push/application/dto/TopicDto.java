package com.tpay.domains.push.application.dto;

import lombok.*;

import java.util.List;

public class TopicDto {
    @Getter
    public static class Request {
        private List<String> tokens;
        private String topic;
    }
}
