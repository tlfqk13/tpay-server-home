package com.tpay.domains.refund.application.dto;

import com.tpay.domains.refund.domain.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

public class RefundPaymentDto {

    @Getter
    @Builder
    public static class Response{
      private RefundDetailDto.Response detailRefundInfo;
      private RefundPaymentInfoDto.Response detailPaymentInfo;
      private PaymentStatus paymentStatus;
    }

    @Getter
    public static class Request{
        private RefundPaymentInfoDto.Request detailPaymentInfo;
        private PaymentStatus paymentStatus;
    }
}
