package com.tpay.domains.vat.application.dto;


public interface VatTotalResponseInterface {
    String getTotalCount();

    String getTotalAmount();

    String getTotalVat();

    String getTotalRefund();
}
