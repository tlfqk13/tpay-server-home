package com.tpay.domains.franchisee.application.dto;

import lombok.Getter;

@Getter
public class PasswordResetRequest {
  private String businessNumber;
  private String password;
}
