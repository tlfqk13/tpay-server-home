package com.tpay.domains.refund.application.dto;

import lombok.Getter;

@Getter
public class RefundInquiryRequest {
  private String name;
  private String orderNumber;
  private String nationality;
  private String amount;
  private String passportNumber;
}
