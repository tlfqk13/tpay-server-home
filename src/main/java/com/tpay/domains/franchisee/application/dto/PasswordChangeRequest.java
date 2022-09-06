package com.tpay.domains.franchisee.application.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequest {
    private String newPassword;
    private String newPasswordCheck;
}
