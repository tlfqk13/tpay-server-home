package com.tpay.domains.franchisee.application.dto;

import lombok.Getter;

@Getter
public class FranchiseeSignInRequest {
  private String businessNumber;
  private String password;
}
