package com.tpay.domains.external.application.dto;

import lombok.Getter;

@Getter
public class ExternalRefundApprovalRequest {
  private Long customerIndex;
  private Long franchiseeIndex;
  private String amount;
}
