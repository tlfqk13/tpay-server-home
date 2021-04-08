package com.tpay.domains.franchisee.application.dto;

import lombok.Getter;

@Getter
public class FranchiseeUpdateRequest {
  private Long id;
  private String storeName;
  private String storeAddress;
  private String businessNumber;
  private String productCategory;
}
