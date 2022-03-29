package com.tpay.domains.auth.application.dto;

import com.tpay.commons.util.UserSelector;
import lombok.Getter;

@Getter
public class SignInRequest {
    private UserSelector userSelector;
    private String businessNumber;
    private String userId;
    private String password;
    private String pushToken;
}
