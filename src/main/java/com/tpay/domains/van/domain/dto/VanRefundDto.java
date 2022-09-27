package com.tpay.domains.van.domain.dto;

import com.tpay.domains.refund_core.application.dto.RefundAfterBaseDto;
import com.tpay.domains.refund_core.application.dto.RefundItemDto;
import lombok.Value;

import java.util.List;

public class VanRefundDto {

    @Value
    public static class Request {

        RefundAfterBaseDto refundAfterBaseInfo;
        String paymentMethod;
        String paymentInfo;
        String paymentBrand;
        List<RefundItemDto.Request> refundItems;
    }
}
