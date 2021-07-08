package com.tpay.domains.sale.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class SaleFindResponse {
    private Long refundId;
    private Long saleId;
    private String totalRefund;
    private String orderNumber;
    private String saleDate;
}
