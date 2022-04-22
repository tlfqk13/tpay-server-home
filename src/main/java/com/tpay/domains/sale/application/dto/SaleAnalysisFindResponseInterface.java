package com.tpay.domains.sale.application.dto;

public interface SaleAnalysisFindResponseInterface {
    String getDate();

    Long getTotalAmount();

    Long getTotalRefund();

    Long getActualAmount();

    Long getSaleCount();

    Long getCancelCount();
}
