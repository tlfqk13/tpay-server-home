package com.tpay.domains.auth.application.dto;

import com.tpay.commons.util.UserSelector;
import lombok.Getter;

public class AccountDeleteDto {

    @Getter
    public static class Request{
        private UserSelector userSelector;
        private Long franchiseeIndex;
        private Long employeeIndex;
    }

}
