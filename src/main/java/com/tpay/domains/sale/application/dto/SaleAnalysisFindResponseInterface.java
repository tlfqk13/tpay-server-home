package com.tpay.domains.sale.application.dto;

import java.time.LocalDate;

public interface SaleAnalysisFindResponseInterface {
    LocalDate getFormatDate();

    Long getTotalAmount();

    Long getTotalRefund();

    Long getActualAmount();

    Long getSaleCount();

    Long getCancelCount();
}
