package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FranchiseeApplicantRepositoryCustom {
    Page<FranchiseeApplicantDto.Response> findFranchiseeAllFilter(Pageable pageable, String searchKeyword
            , FranchiseeStatus franchiseeStatus, boolean isRead, boolean isBusinessNumber);

    Page<FranchiseeApplicantDto.Response> findFranchiseeAllFilter(Pageable pageable, String searchKeyword, boolean isRead, boolean isBusinessNumber);
    Page<FranchiseeApplicantDto.Response> findFranchiseeAll(Pageable pageable, String searchKeyword,boolean isBusinessNumber);


}

