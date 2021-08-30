package com.tpay.domains.franchisee_applicant.domain;

import lombok.Getter;

@Getter
public enum StoreStatus {
  WAIT,
  ACCEPTED,
  REJECTED,
  CANCEL;
}
