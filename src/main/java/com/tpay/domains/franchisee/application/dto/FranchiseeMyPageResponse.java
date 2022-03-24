package com.tpay.domains.franchisee.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

import java.time.LocalDateTime;

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
    private FranchiseeStatus franchiseeStatus;

    private String sellerName;
    private String storeTel;
    private String email;
    private String signboard;
    private String storeNumber;
}
