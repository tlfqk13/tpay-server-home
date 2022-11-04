package com.tpay.domains.franchisee_applicant.application.dto;


import lombok.Getter;


@Getter
public class FranchiseeApplicantUpdateDto {
    @Getter
    public static class taxFreeStoreNumberRequest{
        boolean taxFreeStoreNumber;
    }

    @Getter
    public static class balancePercentageRequest {
        double balancePercentage;
    }

    @Getter
    public static class refundAfterFranchiseeRequest{
        boolean refundAfterFranchisee;
    }
}
