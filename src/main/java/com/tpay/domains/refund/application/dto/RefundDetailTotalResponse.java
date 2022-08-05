package com.tpay.domains.refund.application.dto;


import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class RefundDetailTotalResponse {

    private String totalAmount;
    private String totalCount;
    private String totalRefund;
    private String totalCancel;
    private String totalActualAmount;
}
