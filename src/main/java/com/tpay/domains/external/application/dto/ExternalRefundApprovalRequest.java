package com.tpay.domains.external.application.dto;

import lombok.Getter;

@Getter
public class ExternalRefundApprovalRequest {
  private String passportNumber;
  private String franchiseeNumber;
  private String amount;
}
