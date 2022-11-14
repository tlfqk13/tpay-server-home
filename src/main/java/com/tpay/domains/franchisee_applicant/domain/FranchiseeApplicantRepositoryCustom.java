package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface FranchiseeApplicantRepositoryCustom {
    Page<FranchiseeApplicantDto.Response> findBusinessNumberFromDsl(PageRequest pageRequest, String searchKeyword
            , FranchiseeStatus franchiseeStatus, boolean isRead, boolean isBusinessNumber);

    Page<FranchiseeApplicantDto.Response> findBusinessNumberFromDsl(PageRequest pageRequest, String searchKeyword, boolean isRead, boolean isBusinessNumber);
}

