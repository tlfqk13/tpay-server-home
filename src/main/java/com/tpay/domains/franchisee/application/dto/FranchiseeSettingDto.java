package com.tpay.domains.franchisee.application.dto;

import com.tpay.commons.util.SettingSelector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class FranchiseeSettingDto {

    @Getter
    public static class Request {
        private SettingSelector settingSelector;
        private boolean isActiveSound;
        private boolean isActiveVibration;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Response {
        private String message;
    }
}
