package com.tpay.domains.franchisee_applicant.application.dto;

import lombok.Getter;

@Getter
public class DetailFranchiseeInfo {

    // Franchisee
    private String storeName;
    private String sellerName;
    private String businessNumber;
    private String storeTel;
    private String email;
    private String signboard;
    private String productCategory;
    private String storeNumber;
    private String storeAddressBasic;
    private String storeAddressDetail;
    // Applicants
    private String taxFreeStoreNumber;
    private String bankName;
    private String bankAccount;
    private String withdrawalDate;

    private double balancePercentage;
}
