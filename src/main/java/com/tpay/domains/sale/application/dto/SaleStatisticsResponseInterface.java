package com.tpay.domains.sale.application.dto;

public interface SaleStatisticsResponseInterface {
    String getTotalAmount();

    String getTotalActualAmount();

    String getTotalRefund();

    String getTotalCount();

    String getTotalCancel();
}
