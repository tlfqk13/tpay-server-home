package com.tpay.domains.refund.application.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundCancelResponse {
    private Long userIndex;
    private RefundResponse refundResponse;
}
