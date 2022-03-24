package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantStatusInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeStatusFindService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;

    public FranchiseeApplicantStatusInfo findByIndex(Long franchiseeApplicantIndex) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);

        return FranchiseeApplicantStatusInfo.builder()
            .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
            .build();
    }
}
