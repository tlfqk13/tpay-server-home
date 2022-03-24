package com.tpay.domains.franchisee.application.dto.vat;


public interface FranchiseeVatDetailResponseInterface {
    String getPurchaseSerialNumber();

    String getSaleDate();

    String getTakeoutConfirmNumber();

    String getRefundAmount();

    String getAmount();

    String getVat();
}
