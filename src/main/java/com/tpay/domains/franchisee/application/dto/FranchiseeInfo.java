package com.tpay.domains.franchisee.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeInfo {
  private Long franchiseeIndex;
  private String memberName;
  private String memberNumber;
  private String businessNumber;
  private String storeName;
  private String sellerName;
  private String sellerTel;
  private String productCategory;

  public static FranchiseeInfo of(FranchiseeEntity franchiseeEntity) {
    return FranchiseeInfo.builder()
        .franchiseeIndex(franchiseeEntity.getId())
        .memberName(franchiseeEntity.getMemberName())
        .memberNumber(franchiseeEntity.getMemberNumber())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeName(franchiseeEntity.getStoreName())
        .sellerName(franchiseeEntity.getSellerName())
        .sellerTel(franchiseeEntity.getStoreTel())
        .productCategory(franchiseeEntity.getProductCategory())
        .build();
  }
}
