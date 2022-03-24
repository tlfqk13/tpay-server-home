package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantRejectService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;

    @Transactional
    public void reject(Long franchiseeApplicantIndex, String rejectReason) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);

        franchiseeApplicantEntity.reject(rejectReason);
    }
}
