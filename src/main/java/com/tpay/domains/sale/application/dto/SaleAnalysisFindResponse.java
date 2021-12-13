package com.tpay.domains.sale.application.dto;

public interface SaleAnalysisFindResponse {
  String getDate();

  String getTotalAmount();

  String getTotalVat();

  String getTotalRefund();

  String getTotalPoint();

  String getActualAmount();

  long getSaleCount();

  long getCancelCount();
}
