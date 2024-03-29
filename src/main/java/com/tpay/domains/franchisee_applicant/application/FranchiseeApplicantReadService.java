package com.tpay.domains.franchisee_applicant.application;


import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantReadService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;

    @Transactional
    public boolean read(Long franchiseeApplicantIndex) {
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);
        boolean result = franchiseeApplicantEntity.read();
        return result;
    }
}
