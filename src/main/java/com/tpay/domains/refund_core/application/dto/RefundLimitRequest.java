package com.tpay.domains.refund_core.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundLimitRequest {
    private String name;
    private String passportNumber;
    private String nationality;
    private String totalAmount;
    private String saleDate;
}
