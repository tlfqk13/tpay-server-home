package com.tpay.domains.refund_core.application.dto;

import lombok.Value;

public class RefundItemDto {

    @Value
    public static class Request {
        String docId;
        String refundAmount;
    }
}
