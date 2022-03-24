package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantSaveService {

    private final FranchiseeApplicantRepository franchiseeApplicantRepository;

    @Transactional
    public FranchiseeApplicantEntity save(FranchiseeEntity franchiseeEntity) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            FranchiseeApplicantEntity.builder().franchiseeEntity(franchiseeEntity).build();
        return franchiseeApplicantRepository.save(franchiseeApplicantEntity);
    }
}
