package com.tpay.domains.franchisee_applicant.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeApplicantInfo {
  private Long franchiseeApplicantIndex;
  private FranchiseeStatus franchiseeStatus;
  private String rejectReason;

  private String memberName;
  private String businessNumber;
  private String storeName;
  private String storeAddress;
  private String sellerName;
  private String storeTel;
  private String productCategory;
  private String createdDate;

  public static FranchiseeApplicantInfo toResponse(FranchiseeApplicantEntity franchiseeApplicantEntity) {
    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

    return FranchiseeApplicantInfo.builder()
        .franchiseeApplicantIndex(franchiseeApplicantEntity.getId())
        .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
        .rejectReason(franchiseeApplicantEntity.getRejectReason())
        .memberName(franchiseeEntity.getMemberName())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeName(franchiseeEntity.getStoreName())
        .storeAddress(franchiseeEntity.getStoreAddress())
        .sellerName(franchiseeEntity.getSellerName())
        .storeTel(franchiseeEntity.getStoreTel())
        .productCategory(franchiseeEntity.getProductCategory())
        .createdDate(franchiseeEntity.getCreatedDate().toString())
        .build();
  }
}
