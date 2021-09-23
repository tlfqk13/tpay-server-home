package com.tpay.domains.refund_core.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundCancelRequest {
  private String purchaseSequenceNumber;
  private String takeOutNumber;
  private String name;
  private String orderNumber;
  private String nationality;
  private String amount;
  private String passportNumber;
}
