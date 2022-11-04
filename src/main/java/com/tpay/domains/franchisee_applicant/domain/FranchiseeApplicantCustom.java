package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FranchiseeApplicantCustom {

    Page<FranchiseeApplicantDto.Response> filterAndBusinessNumberDsl(List<Boolean> booleanList, List<FranchiseeStatus> franchiseeStatusList, Pageable pageable, String searchKeyword);
}
