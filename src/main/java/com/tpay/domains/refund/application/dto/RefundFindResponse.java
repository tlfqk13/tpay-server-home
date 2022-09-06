package com.tpay.domains.refund.application.dto;


import com.tpay.domains.refund.domain.RefundStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundFindResponse {
    private Long refundIndex;
    private String orderNumber;
    private LocalDateTime createdDate;
    private String totalAmount;
    private String totalRefund;
    private String actualAmount;
    private RefundStatus refundStatus;

    public RefundFindResponse(RefundFindResponseInterface refundFindResponseInterface){
        this.refundIndex = refundFindResponseInterface.getRefundIndex();
        this.orderNumber = refundFindResponseInterface.getOrderNumber();
        this.createdDate = refundFindResponseInterface.getCreatedDate();
        this.totalAmount = refundFindResponseInterface.getTotalAmount();
        this.totalRefund = refundFindResponseInterface.getTotalRefund();
        this.actualAmount = refundFindResponseInterface.getActualAmount();
        this.refundStatus = refundFindResponseInterface.getRefundStatus();
    }
}
