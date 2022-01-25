package com.tpay.domains.franchisee_applicant.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

import java.time.LocalDateTime;

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
  private FranchiseeStatus franchiseeStatus;
  private String signboard;
  private String productCategory;
  private String storeNumber;
  private String storeAddressBasic;
  private String storeAddressDetail;
  private LocalDateTime createdDate;
  private Boolean isRead;
  // Applicants
  private String imageUrl;
  private String taxFreeStoreNumber;
  private String bankName;
  private String bankAccount;
  private String withdrawalDate;
  private String rejectReason;

}
