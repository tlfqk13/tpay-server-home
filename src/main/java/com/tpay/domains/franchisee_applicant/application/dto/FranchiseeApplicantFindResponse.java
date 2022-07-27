package com.tpay.domains.franchisee_applicant.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeApplicantFindResponse {

    private int totalPage;
    private List<FranchiseeApplicantInfo> franchiseeApplicantInfoList;
}
