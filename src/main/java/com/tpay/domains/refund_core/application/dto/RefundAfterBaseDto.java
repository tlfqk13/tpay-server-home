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
    @Builder.Default
    private String locaCode = "ICN";
    @Builder.Default
    private String kioskBsnmCode = "";
    @Builder.Default
    private String kioskCode = "";
    @Builder.Default
    private String counterTypeCode = "";
    private RefundAfterMethod refundAfterMethod;
    private String refundFinishDate;
    private boolean retry;
}
