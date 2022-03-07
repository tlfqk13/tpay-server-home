package com.tpay.domains.auth.application.dto;

import com.tpay.commons.util.UserSelector;
import lombok.Getter;

@Getter
public class SignOutRequest {
  private UserSelector userSelector;
  private Long franchiseeIndex;
  private Long employeeIndex;
}
