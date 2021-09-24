package com.tpay.domains.order.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrderGroupingResponse {
    private String totalAmount;
    private String totalRefund;
    private String totalVAT;
    private String saleDate;
}
