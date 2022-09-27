package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.refund.domain.RefundAfterMethod;
import lombok.Value;

import java.util.List;

public class RefundAfterDto {

    @Value
    public static class Request {
        RefundAfterBaseDto refundAfterInfo;
        RefundItemDto.Request refundItem;
    }
}
