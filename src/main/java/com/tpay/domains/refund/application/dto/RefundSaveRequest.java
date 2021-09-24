package com.tpay.domains.refund.application.dto;

import lombok.Getter;

@Getter
public class RefundSaveRequest {
  private Long franchiseeIndex;
  private Long customerIndex;
  private String price;
}
