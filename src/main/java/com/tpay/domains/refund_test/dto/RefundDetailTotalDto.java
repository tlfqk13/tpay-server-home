package com.tpay.domains.refund_test.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.refund.application.dto.RefundDetailTotalResponse;
import lombok.*;

import java.util.List;

public class RefundDetailTotalDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class Response{
        String saleCount;
        String cancelCount;
        String sumOfTotalAmount;
        String sumOfTotalRefund;
        String sumOfTotalActualAmount;

        private RefundDetailTotalResponse totalRefundData;
        List<RefundFindDto.Response> refundList;

        @QueryProjection
        public Response(String saleCount, String cancelCount, String sumOfTotalAmount, String sumOfTotalRefund, String sumOfTotalActualAmount){
            this.saleCount = saleCount;
            this.cancelCount = cancelCount;
            this.sumOfTotalAmount = sumOfTotalAmount;
            this.sumOfTotalRefund = sumOfTotalRefund;
            this.sumOfTotalActualAmount = sumOfTotalActualAmount;
        }
    }
}
