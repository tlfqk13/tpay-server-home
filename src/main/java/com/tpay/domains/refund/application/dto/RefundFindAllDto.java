package com.tpay.domains.refund.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.refund.domain.PaymentStatus;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.Getter;

import java.time.LocalDateTime;

public class RefundFindAllDto {

    @Getter
    public static class Response{
        Long refundIndex;
        String customerName;
        String customerNational;
        LocalDateTime createdDate;
        String totalAmount;
        String totalRefund;
        int actualAmount;
        RefundStatus refundStatus;
        String businessNumber;
        String storeName;
        PaymentStatus paymentStatus;
        DepartureStatus departureStatus;

        @QueryProjection
        public Response(Long refundIndex, String customerName, String customerNational,LocalDateTime createdDate,
                        String totalAmount,String totalRefund,int actualAmount,RefundStatus refundStatus,
                        String businessNumber, String storeName, PaymentStatus paymentStatus, DepartureStatus departureStatus){
            this.refundIndex = refundIndex;
            this.customerName = customerName;
            this.customerNational = customerNational;
            this.createdDate = createdDate;
            this.totalAmount = totalAmount;
            this.totalRefund = totalRefund;
            this.actualAmount = actualAmount;
            this.refundStatus = refundStatus;
            this.businessNumber = businessNumber;
            this.storeName = storeName;
            this.paymentStatus = paymentStatus;
            this.departureStatus = departureStatus;
        }
    }
}
