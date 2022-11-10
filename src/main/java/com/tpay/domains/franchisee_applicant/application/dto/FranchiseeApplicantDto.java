package com.tpay.domains.franchisee_applicant.application.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FranchiseeApplicantDto {


    @Getter
    @Builder
    public static class Response{
        private Long franchiseeApplicantIndex;
        private FranchiseeStatus franchiseeStatus;
        private String businessNumber;
        private String storeName;
        private String sellerName;
        private LocalDateTime createdDate;
        private boolean isRefundOnce;
        private boolean isRead;

        @QueryProjection
        public Response(Long franchiseeApplicantIndex, FranchiseeStatus franchiseeStatus,String businessNumber
                , String storeName, String sellerName,LocalDateTime createdDate
                ,boolean isRefundOnce, boolean isRead){
            this.franchiseeApplicantIndex = franchiseeApplicantIndex;
            this.franchiseeStatus = franchiseeStatus;
            this.businessNumber = businessNumber;
            this.storeName = storeName;
            this.sellerName = sellerName;
            this.createdDate = createdDate;
            this.isRefundOnce = isRefundOnce;
            this.isRead = isRead;
        }
    }
}
