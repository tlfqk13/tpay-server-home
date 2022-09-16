package com.tpay.domains.franchisee_applicant.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import lombok.Getter;

@Getter
public class FranchiseeApplicantDetailUpdateResponse {
    // Franchisee
    private final String storeName;
    private final String sellerName;
    private final String businessNumber;
    private final String storeTel;
    private final String email;
    private final String signboard;
    private final String productCategory;
    private final String storeNumber;
    private final String storeAddressBasic;
    private final String storeAddressDetail;
    // Applicants

    private final String taxFreeStoreNumber;
    private final String bankName;
    private final String bankAccount;
    private final String withdrawalDate;

    private final double balancePercentage;

    public FranchiseeApplicantDetailUpdateResponse(FranchiseeEntity franchiseeEntity
            , FranchiseeBankEntity franchiseeBankEntity
            , String taxFreeStoreNumberUpdate){
        this.storeName = franchiseeEntity.getStoreName();
        this.sellerName = franchiseeEntity.getSellerName();
        this.businessNumber = franchiseeEntity.getBusinessNumber();
        this.storeTel = franchiseeEntity.getStoreTel();
        this.email = franchiseeEntity.getEmail();
        this.signboard = franchiseeEntity.getSignboard();
        this.productCategory = franchiseeEntity.getProductCategory();
        this.storeNumber = franchiseeEntity.getStoreNumber();
        this.storeAddressBasic = franchiseeEntity.getStoreAddressBasic();
        this.storeAddressDetail = franchiseeEntity.getStoreAddressDetail();
        this.taxFreeStoreNumber = taxFreeStoreNumberUpdate;
        this.bankName = franchiseeBankEntity.getBankName();
        this.bankAccount = franchiseeBankEntity.getAccountNumber();
        this.withdrawalDate = franchiseeBankEntity.getWithdrawalDate();
        this.balancePercentage = franchiseeEntity.getBalancePercentage();

    }
    

}
