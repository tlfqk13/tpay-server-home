package com.tpay.domains.sale.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SaleStatisticsCurrentResponse {
  private String totalAmount;
  private String totalActualAmount;
  private String totalRefund;
  private String totalCount;
  private String totalCancel;
}
