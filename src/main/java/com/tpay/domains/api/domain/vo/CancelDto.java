package com.tpay.domains.api.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CancelDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String purchaseSequenceNumber;
    }
}
