package com.tpay.domains.auth.presentation;

import com.tpay.commons.util.SignInSelector;
import lombok.Getter;

@Getter
public class SignInRequest {
  private SignInSelector signInSelector;
  private String businessNumber;
  private String userId;
  private String password;
}
