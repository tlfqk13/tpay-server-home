package com.tpay.domains.franchisee_applicant.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeApplicantInfo {
  private Long franchiseeApplicantIndex;
  private FranchiseeStatus franchiseeStatus;
//  private String rejectReason;

  private String memberName;
  private String businessNumber;
  private String storeName;
//  private String storeAddressNumber;
//  private String storeAddressBasic;
//  private String storeAddressDetail;
  private String sellerName;
//  private String storeTel;
//  private String productCategory;
  private String createdDate;
  private String isRefundOnce;

  public static FranchiseeApplicantInfo toResponse(FranchiseeApplicantEntity franchiseeApplicantEntity) {
    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

    return FranchiseeApplicantInfo.builder()
        .franchiseeApplicantIndex(franchiseeApplicantEntity.getId())
        .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
//        .rejectReason(franchiseeApplicantEntity.getRejectReason())
        .memberName(franchiseeEntity.getMemberName())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeName(franchiseeEntity.getStoreName())
//        .storeAddressNumber(franchiseeEntity.getStoreAddressNumber())
//        .storeAddressBasic(franchiseeEntity.getStoreAddressBasic())
//        .storeAddressDetail(franchiseeEntity.getStoreAddressDetail())
        .sellerName(franchiseeEntity.getSellerName())
//        .storeTel(franchiseeEntity.getStoreTel())
//        .productCategory(franchiseeEntity.getProductCategory())
        .createdDate(franchiseeEntity.getCreatedDate().toString())
        .isRefundOnce(franchiseeEntity.getIsRefundOnce())
        .build();
  }
}
