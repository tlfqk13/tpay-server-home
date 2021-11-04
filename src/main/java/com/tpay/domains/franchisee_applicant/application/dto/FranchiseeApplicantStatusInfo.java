package com.tpay.domains.franchisee_applicant.application.dto;

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
public class FranchiseeApplicantStatusInfo {
  private FranchiseeStatus franchiseeStatus;
}
