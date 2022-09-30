package com.tpay.domains.van.domain.dto;

import com.tpay.domains.refund_core.application.dto.RefundItemDto;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class VanRefundAfterDto {
    VanRefundAfterBaseDto refundAfterBaseInfo;
    String paymentMethod;
    String paymentInfo;
    String paymentBrand;
    List<RefundItemDto.Request> refundItems;
}
