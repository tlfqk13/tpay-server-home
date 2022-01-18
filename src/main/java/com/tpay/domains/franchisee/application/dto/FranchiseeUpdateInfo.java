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
  private String storeAddressNumber;
  private String storeAddressBasic;
  private String storeAddressDetail;

  private String businessNumber;
  private String productCategory;

  private String signboard; // 간판명
  private String storeNumber; // 매장번호
  private String email; // email 주소
}
