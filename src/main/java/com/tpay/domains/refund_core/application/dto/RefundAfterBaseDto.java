package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.refund.domain.RefundAfterMethod;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefundAfterBaseDto {
    String cusCode;
    String locaCode;
    String kioskBsnmCode;
    String kioskCode;
    String counterTypeCode;
    RefundAfterMethod refundAfterMethod;
    String refundFinishDate;
    boolean retry;
}
