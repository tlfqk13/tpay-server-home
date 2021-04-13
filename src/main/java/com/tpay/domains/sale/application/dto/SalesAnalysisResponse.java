package com.tpay.domains.sale.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SalesAnalysisResponse {
  private String saleDate;
  private String totalAmount;
  private String totalRefund;
  private String totalVAT;
  private int saleCount;
}
