package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.NonBatchPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantRejectService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final NonBatchPushService nonBatchPushService;

    @Transactional
    public void reject(Long franchiseeApplicantIndex, String rejectReason) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);
        nonBatchPushService.nonBatchPushNSave(PushCategoryType.CASE_THREE, franchiseeApplicantEntity.getFranchiseeEntity().getId());
        franchiseeApplicantEntity.reject(rejectReason);
    }
}
