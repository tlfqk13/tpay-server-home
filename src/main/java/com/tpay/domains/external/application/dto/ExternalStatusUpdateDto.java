package com.tpay.domains.external.application.dto;

import com.tpay.domains.external.domain.ExternalRefundStatus;
import lombok.Getter;

@Getter
public class ExternalStatusUpdateDto {
    Long externalRefundIndex;
    ExternalRefundStatus externalRefundStatus;
    String payment;
}
