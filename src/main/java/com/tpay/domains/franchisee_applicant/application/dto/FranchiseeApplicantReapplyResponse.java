package com.tpay.domains.franchisee_applicant.application.dto;


import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeApplicantReapplyResponse {
    private String uploadImage;
    private FranchiseeBankInfo franchiseeBankInfo;
}
