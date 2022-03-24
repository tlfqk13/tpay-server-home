package com.tpay.domains.auth.application.dto;

import lombok.Getter;

@Getter
public class FranchiseeSignInRequest {
    private String businessNumber;
    private String password;
}
