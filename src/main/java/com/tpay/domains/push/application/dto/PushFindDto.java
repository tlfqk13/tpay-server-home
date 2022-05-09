package com.tpay.domains.push.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class PushFindDto {

    @Getter
    public static class Request {
        private Long pushIndex;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long pushIndex;
        private String title;
        private LocalDateTime createdDate;
        private String body;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FindAllResponse {
        private List<AdminPushResponse> responseList;
    }
}
