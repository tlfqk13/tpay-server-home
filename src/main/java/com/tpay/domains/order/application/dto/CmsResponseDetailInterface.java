package com.tpay.domains.order.application.dto;

public interface CmsResponseDetailInterface {
    Long getFranchiseeIndex();

    String getTotalCount();

    String getTotalAmount();

    String getTotalVat();

    String getTotalCommission();

    String getSellerName();

    String getBankName();

    String getAccountNumber();

    String getWithdrawalDate();

    String getTotalBill();
}
