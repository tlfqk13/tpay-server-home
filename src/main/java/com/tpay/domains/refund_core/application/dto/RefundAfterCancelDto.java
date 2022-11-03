package com.tpay.domains.refund_core.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class RefundAfterCancelDto {

    @NoArgsConstructor
    @Getter
    public static class Request {
        String tkOutNum;
    }
}
