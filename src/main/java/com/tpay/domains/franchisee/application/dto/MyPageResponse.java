package com.tpay.domains.franchisee.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyPageResponse {
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


    public MyPageResponse(FranchiseeEntity franchiseeEntity, FranchiseeApplicantEntity franchiseeApplicantEntity, Long totalSalesAmount) {
        this.createdDate = franchiseeEntity.getCreatedDate();
        this.businessNumber = franchiseeEntity.getBusinessNumber();
        this.storeName = franchiseeEntity.getStoreName();
        this.storeAddressNumber = franchiseeEntity.getStoreAddressNumber();
        this.storeAddressBasic = franchiseeEntity.getStoreAddressBasic();
        this.storeAddressDetail = franchiseeEntity.getStoreAddressDetail();
        this.totalSalesAmount = totalSalesAmount;
        this.totalPoint = franchiseeEntity.getBalance();
        this.productCategory = franchiseeEntity.getProductCategory();
        this.franchiseeStatus = franchiseeApplicantEntity.getFranchiseeStatus();
        this.sellerName = franchiseeEntity.getSellerName();
        this.storeTel = franchiseeEntity.getStoreTel();
        this.email = franchiseeEntity.getEmail();
        this.signboard = franchiseeEntity.getSignboard();
        this.storeNumber = franchiseeEntity.getStoreNumber();
    }
}
