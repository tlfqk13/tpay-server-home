package com.tpay.domains.refund_core.application.dto;

import lombok.Value;

public class RefundAfterDto {

    @Value
    public static class Request {
        RefundAfterBaseDto refundAfterInfo;
        RefundItemDto.Request refundItem;
    }
}
