package com.tpay.domains.vat.application.dto;


public interface VatDetailResponseInterface {
    String getPurchaseSerialNumber();

    String getSaleDate();

    String getTakeoutConfirmNumber();

    String getRefundAmount();

    String getAmount();

    String getVat();

    String getCustomerName();

    String getCustomerNational();
}
