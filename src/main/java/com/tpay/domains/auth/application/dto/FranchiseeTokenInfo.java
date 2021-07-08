package com.tpay.domains.auth.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeTokenInfo {
  private Long franchiseeIndex;
  private String accessToken;
  private String refreshToken;
}
