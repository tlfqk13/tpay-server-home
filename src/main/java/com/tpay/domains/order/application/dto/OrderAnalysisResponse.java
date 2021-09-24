package com.tpay.domains.order.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrderAnalysisResponse {
  private String saleDate;
  private String totalAmount;
  private String totalRefund;
  private String totalVAT;
  private int saleCount;
}
