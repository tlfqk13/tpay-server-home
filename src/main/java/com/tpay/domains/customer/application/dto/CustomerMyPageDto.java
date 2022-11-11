package com.tpay.domains.customer.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerMyPageDto {

    @Getter
    @Builder
    public static class Response{
        private int totalRefundCompleted;
        private String refundInformation;
    }
}
