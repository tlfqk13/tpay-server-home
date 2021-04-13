package com.tpay.domains.refund.application.dto;

import lombok.Getter;

@Getter
public class RefundRegisterRequest {
    private Long franchiseeIndex;
    private Long customerIndex;
    private String price;
}
