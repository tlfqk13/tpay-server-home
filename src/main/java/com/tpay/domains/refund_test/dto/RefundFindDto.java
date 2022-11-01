package com.tpay.domains.refund_test.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.Getter;

import java.time.LocalDateTime;

public class RefundFindDto {

    @Getter
    public static class Response{

        Long refundIndex;
        RefundStatus refundStatus;
        LocalDateTime createdDate;
        String totalAmount;
        String totalRefund;
        int actualAmount;

        @QueryProjection
        public Response(Long refundIndex, RefundStatus refundStatus, LocalDateTime createdDate, String totalAmount,
                        String totalRefund, int actualAmount
                        ){
            this.refundIndex = refundIndex;
            this.refundStatus = refundStatus;
            this.createdDate = createdDate;
            this.totalAmount = totalAmount;
            this.totalRefund = totalRefund;
            this.actualAmount = actualAmount;
        }
    }
}
