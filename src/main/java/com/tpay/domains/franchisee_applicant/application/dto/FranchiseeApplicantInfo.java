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
    private String memberName;
    private String businessNumber;
    private String storeName;
    private String sellerName;
    private String createdDate;
    private Boolean isRefundOnce;
    private Boolean isRead;

    public static FranchiseeApplicantInfo toResponse(FranchiseeApplicantEntity franchiseeApplicantEntity) {
        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
        return FranchiseeApplicantInfo.builder()
            .franchiseeApplicantIndex(franchiseeApplicantEntity.getId())
            .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
            .memberName(franchiseeEntity.getMemberName())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .storeName(franchiseeEntity.getStoreName())
            .sellerName(franchiseeEntity.getSellerName())
            .createdDate(franchiseeEntity.getCreatedDate().toString())
            .isRefundOnce(franchiseeEntity.getIsRefundOnce())
            .isRead(franchiseeApplicantEntity.getIsRead())
            .build();
    }
}
