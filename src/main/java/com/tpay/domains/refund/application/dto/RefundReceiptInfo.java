package com.tpay.domains.refund.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RefundReceiptInfo {
    @Getter
    @Builder
    public static class Response {
        List<RefundReceiptDto.Response> refundReceiptList;
    }
}

