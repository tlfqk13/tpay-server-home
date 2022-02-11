package com.tpay.domains.refund.application.dto;

import lombok.Getter;

@Getter
public class RefundCustomerInfoRequest {
  private String nationality;
  private String passportNumber;
  private String name;
}
