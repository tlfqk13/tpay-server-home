package com.tpay.domains.refund_core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefundData {
    private String paymentPrice;
    private String originPrice;
    private String refundPrice;

    public RefundData() {
        this.paymentPrice = "";
        this.originPrice = "";
        this.refundPrice = "";
    }
}
