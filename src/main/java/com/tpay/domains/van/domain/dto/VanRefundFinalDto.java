package com.tpay.domains.van.domain.dto;

import com.tpay.domains.refund_core.application.dto.RefundItemDto;
import lombok.Builder;
import lombok.Value;

import java.util.List;

public class VanRefundFinalDto {

    @Value
    @Builder
    public static class Request {
        VanRefundAfterBaseDto baseRefundAfterDto;
        String refundFinishDate;
        String retryYn;
        List<RefundItemDto.Request> refundItems;
    }
}
