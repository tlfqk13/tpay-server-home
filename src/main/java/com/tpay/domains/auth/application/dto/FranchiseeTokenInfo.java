package com.tpay.domains.auth.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
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
  private FranchiseeStatus franchiseeStatus;
  private String rejectReason;
  private String accessToken;
  private String refreshToken;
}
