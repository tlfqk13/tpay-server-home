package com.tpay.domains.refund.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundSaveRequest {
    private Long customerIndex;
    private String price;
    private String refund;
    private Device device;
}
