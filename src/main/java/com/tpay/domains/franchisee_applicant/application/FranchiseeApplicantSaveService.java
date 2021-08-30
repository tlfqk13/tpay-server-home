package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantSaveService {

  private final FranchiseeApplicantRepository franchiseeApplicantRepository;

  @Transactional
  public FranchiseeApplicantEntity getNewOne() {
    FranchiseeApplicantEntity franchiseeApplicantEntity = FranchiseeApplicantEntity.builder().build();
    return franchiseeApplicantRepository.save(franchiseeApplicantEntity);
  }

}
