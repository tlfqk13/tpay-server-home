package com.tpay.domains.sale.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SaleFindResponse {
    private String totalRefund;
    private String orderNumber;
    private String saleDate;
}
