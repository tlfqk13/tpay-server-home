package com.tpay.domains.refund.application.dto;


import lombok.Getter;

@Getter
public class RefundCustomerDateRequest {
  private String orderCheck;
  private String startDate;
  private String endDate;
}
