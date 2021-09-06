package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeReApplyService {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;

  @Transactional
  public FranchiseeApplicantInfo reapply(String businessNumber) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantFindService.findByBusinessNumber(businessNumber);

    franchiseeApplicantEntity.reapply();
    return FranchiseeApplicantInfo.toResponse(franchiseeApplicantEntity);
  }
}
