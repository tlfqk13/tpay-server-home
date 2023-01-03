package com.tpay.domains.refund.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.refund.domain.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class RefundDetailDto {

    @Getter
    @Builder
    public static class Response{
        private LocalDateTime createdDate;
        private String storeName;
        private String customerName;
        private String customerNational;
        private String totalAmount;
        private String totalRefund;
        private DepartureStatus departureStatus;
        private PaymentStatus paymentStatus;

        @QueryProjection
        public Response(LocalDateTime createdDate, String storeName, String customerName, String customerNational,
                        String totalAmount, String totalRefund, DepartureStatus departureStatus,PaymentStatus paymentStatus){
            this.createdDate = createdDate;
            this.storeName = storeName;
            this.customerName = customerName;
            this.customerNational = customerNational;
            this.totalAmount = totalAmount;
            this.totalRefund = totalRefund;
            this.departureStatus = departureStatus;
            this.paymentStatus = paymentStatus;
        }

    }
}
