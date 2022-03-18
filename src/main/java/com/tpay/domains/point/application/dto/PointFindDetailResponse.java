package com.tpay.domains.point.application.dto;


import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import com.tpay.domains.point.domain.PointStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointFindDetailResponse {

  //Franchisee Info
  private String storeName;
  private String sellerName;
  private String businessNumber;
  private String storeTel;
  private String email;
  private String isTaxRefundShop;
  private FranchiseeStatus franchiseeStatus;
  private String signboard;
  private String productCategory;
  private String storeNumber;
  private String storeAddressBasic;
  private String storeAddressDetail;
  private LocalDateTime createdDate;
  private Boolean isRead;

  //Withdrawal Info
  private LocalDateTime requestedDate;
  private PointStatus pointStatus;
  private Long currentPoint;
  private Long amount;
  private Long afterPayment;
  private Boolean isReadTPoint;

  //Bank Info
  private String bankName;
  private String accountNumber;
}
