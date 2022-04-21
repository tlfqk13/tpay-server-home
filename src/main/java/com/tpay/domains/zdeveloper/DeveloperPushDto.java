package com.tpay.domains.zdeveloper;

import lombok.Getter;

public class DeveloperPushDto {

    @Getter
    public static class Request {
        private String pushTypeCategory;
        private Long franchiseeIndex;
    }
}
