package com.tpay.domains.refund.application.dto;


import com.tpay.domains.refund.domain.RefundStatus;
import java.time.LocalDateTime;
import lombok.*;

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
    private RefundStatus refundStatus;
}
