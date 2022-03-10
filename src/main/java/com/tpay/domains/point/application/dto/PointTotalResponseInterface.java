package com.tpay.domains.point.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;

public interface PointTotalResponseInterface {
  String getTotalPoint();
  String getScheduledPoint();
  String getDisappearPoint();
  FranchiseeStatus getFranchiseeStatus();
}
