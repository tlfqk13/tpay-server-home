package com.tpay.domains.franchisee_applicant.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeApplicantFindResponseTest {

    private int totalPage;
    List<FranchiseeApplicantDto.Response> franchiseeApplicantInfoList;
}
