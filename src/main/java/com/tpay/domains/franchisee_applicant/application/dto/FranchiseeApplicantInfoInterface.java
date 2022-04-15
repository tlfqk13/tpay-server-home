package com.tpay.domains.franchisee_applicant.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;

public interface FranchiseeApplicantInfoInterface {
    Long getFranchiseeApplicantIndex();

    FranchiseeStatus getFranchiseeStatus();

    String getMemberName();

    String getBusinessNumber();

    String getStoreName();

    String getSellerName();

    String getCreatedDate();

    Boolean getIsRefundOnce();

    Boolean getIsRead();
}
