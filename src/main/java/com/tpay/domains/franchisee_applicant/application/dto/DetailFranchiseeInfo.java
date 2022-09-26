package com.tpay.domains.franchisee_applicant.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DetailFranchiseeInfo {

    // Franchisee
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
    private String createdDate;
    private Boolean isRead;
    // Applicants
    private String imageUrl;
    private String taxFreeStoreNumber;
    private String bankName;
    private String bankAccount;
    private String withdrawalDate;
    private String rejectReason;

    private double balancePercentage;
}
