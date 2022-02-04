package com.tpay.domains.franchisee.application.dto.cms;

public interface FranchiseeCmsResponseDetailInterface {
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
