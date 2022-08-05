package com.tpay.domains.refund.application.dto;


import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundDetailFindResponse {
    private RefundDetailTotalResponse totalRefundData;
    private List<RefundFindResponse> refundList;
}
