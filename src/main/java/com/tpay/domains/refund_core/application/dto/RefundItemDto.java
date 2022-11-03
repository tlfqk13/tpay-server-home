package com.tpay.domains.refund_core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RefundItemDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        // 현재 String 1개의 정보밖에 없어서 추후 문제 없을시, dto 삭제 후 기본 dto 에 포함
        private String docId;
    }
}
