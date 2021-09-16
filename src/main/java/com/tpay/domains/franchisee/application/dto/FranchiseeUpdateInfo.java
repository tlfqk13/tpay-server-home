package com.tpay.domains.franchisee.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeUpdateInfo {
  private String storeName;
  private String storeAddress;
  private String businessNumber;
  private String productCategory;
}
