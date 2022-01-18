package com.tpay.domains.franchisee.application.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeMyPageResponse {
  private LocalDateTime createdDate;
  private String businessNumber;
  private String storeName;
  private String storeAddressNumber;
  private String storeAddressBasic;
  private String storeAddressDetail;
  private Long totalSalesAmount;
  private Long totalPoint;
  private String productCategory;
}
