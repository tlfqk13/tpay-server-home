package com.tpay.domains.point.application.dto;

import lombok.Builder;
import lombok.Getter;

public class PointTotalDto {

    @Getter
    @Builder
    public static class Response {
        private Long totalPoints;
        private Long scheduledPoints;
        private Long disappearPoints;
    }
}
