package com.tpay.domains.refund.application.dto;


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
}
