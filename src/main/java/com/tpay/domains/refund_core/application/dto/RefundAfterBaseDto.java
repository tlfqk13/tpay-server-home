package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.refund.domain.RefundAfterMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefundAfterBaseDto {
    private String cusCode;
    private String locaCode;
    private String kioskBsnmCode;
    private String kioskCode;
    private String counterTypeCode;
    private RefundAfterMethod refundAfterMethod;
    private String refundFinishDate;
    private boolean retry;
}
