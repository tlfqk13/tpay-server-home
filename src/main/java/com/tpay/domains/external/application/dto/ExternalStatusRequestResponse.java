package com.tpay.domains.external.application.dto;


import com.tpay.domains.refund_core.application.dto.RefundData;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExternalStatusRequestResponse {

    private ExternalResultStatus type;
    private RefundData refundData;
    private String message;
}


