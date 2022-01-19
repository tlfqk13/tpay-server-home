package com.tpay.domains.franchisee_applicant.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeApplicantDetailResponse {
  // Franchisee
  private String storeName;
  private String sellerName;
  private String businessNumber;
  private String storeTel;
  private String email;
  private String isTaxFreeStore;
  private String franchiseeStatus;
  private String signboard;
  private String productCategory;
  private String storeNumber;
  private String storeAddressBasic;
  private String storeAddressDetail;
  // Applicants
  private String imageUrl;
//  private String taxFreeStoreNumber; 지금은 없음
  private String bankName;
  private String bankAccount;
  private String withdrawalDate;
  //etc
  private String rejectReason;
}
