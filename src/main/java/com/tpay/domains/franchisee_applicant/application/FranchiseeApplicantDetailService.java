package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDetailResponse;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantDetailService {

  private final FranchiseeFindService franchiseeFindService;
  private final FranchiseeApplicantFindService franchiseeApplicantFindService;

  public FranchiseeApplicantDetailResponse detail(Long franchiseeApplicantIndex) {
    FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);
    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();


  }
}
