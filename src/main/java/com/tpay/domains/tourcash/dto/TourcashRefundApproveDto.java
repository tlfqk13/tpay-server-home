package com.tpay.domains.tourcash.dto;

import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TourcashRefundApproveDto {
    private Long franchiseeIndex;
    private Long customerIndex;
    private String price;
    private String refund;

    public RefundSaveRequest convertTo() {
        return RefundSaveRequest.builder()
                .customerIndex(getCustomerIndex())
                .price(getPrice())
                .refund(getRefund())
                .build();
    }
}
