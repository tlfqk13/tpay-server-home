package com.tpay.domains.refund.application.dto;

import com.tpay.domains.refund.domain.RefundStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundByCustomerResponse {
    private Long refundIndex;
    private String orderNumber;
    private LocalDateTime createdDate;
    private LocalDate formatDate;
    private String totalAmount;
    private String totalRefund;
    private RefundStatus refundStatus;
    private Long point;


    public static RefundByCustomerResponse from(RefundFindResponseInterface refundFindResponseInterface){
        return RefundByCustomerResponse.builder()
            .refundIndex(refundFindResponseInterface.getRefundIndex())
            .orderNumber(refundFindResponseInterface.getOrderNumber())
            .createdDate(refundFindResponseInterface.getCreatedDate())
            .formatDate(refundFindResponseInterface.getFormatDate())
            .totalAmount(refundFindResponseInterface.getTotalAmount())
            .totalRefund(refundFindResponseInterface.getTotalRefund())
            .refundStatus(refundFindResponseInterface.getRefundStatus())
            .point(refundFindResponseInterface.getPoint())
            .build();
    }
}
