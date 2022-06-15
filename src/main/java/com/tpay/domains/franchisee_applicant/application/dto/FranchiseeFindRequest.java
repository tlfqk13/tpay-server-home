package com.tpay.domains.franchisee_applicant.application.dto;

import lombok.Getter;

@Getter
public class FranchiseeFindRequest {
    private String businessNumber;
    private String franchiseeName;
    private String franchiseeNumber;
    private String balancePercentage;
}
