package com.tpay.domains.franchisee_applicant.domain;

import lombok.Getter;

@Getter
public enum FranchiseeStatus {
  WAIT,
  ACCEPTED,
  REJECTED,
  REAPPLIED,
  CANCEL;
}
