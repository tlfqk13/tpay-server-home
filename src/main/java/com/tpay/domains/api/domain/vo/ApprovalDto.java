package com.tpay.domains.api.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ApprovalDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String franchiseeId;
        private String name;
        private String passport;
        private String nation;
        private String price;
        private String refund;
    }
}
