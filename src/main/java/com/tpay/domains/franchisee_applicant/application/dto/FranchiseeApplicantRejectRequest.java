package com.tpay.domains.franchisee_applicant.application.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FranchiseeApplicantRejectRequest {
    private String rejectReason;
}
