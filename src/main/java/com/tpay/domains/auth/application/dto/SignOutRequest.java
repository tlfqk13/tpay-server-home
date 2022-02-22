package com.tpay.domains.auth.application.dto;

import com.tpay.commons.util.SignInSelector;
import lombok.Getter;

@Getter
public class SignOutRequest {
  private SignInSelector signInSelector;
  private Long franchiseeIndex;
  private Long employeeIndex;
}
