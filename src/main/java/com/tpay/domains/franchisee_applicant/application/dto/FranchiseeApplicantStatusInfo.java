package com.tpay.domains.franchisee_applicant.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeApplicantStatusInfo {
    private FranchiseeStatus franchiseeStatus;
    private Long count;
}
