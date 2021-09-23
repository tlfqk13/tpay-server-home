package com.tpay.domains.order.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class SaleFindResponse {
  private Long refundId;
  private Long saleId;
  private String totalRefund;
  private String orderNumber;
  private String saleDate;
}
