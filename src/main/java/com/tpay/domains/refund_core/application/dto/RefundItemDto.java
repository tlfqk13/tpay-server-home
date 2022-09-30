package com.tpay.domains.refund_core.application.dto;

import lombok.Getter;
import lombok.Value;

public class RefundItemDto {

    @Value
    @Getter
    public static class Request {
        String docId;
        String refundAmount;
    }
}
