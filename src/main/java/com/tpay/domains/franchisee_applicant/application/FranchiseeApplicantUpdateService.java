package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantUpdateService {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;

  @Transactional
  public void reject(Long franchiseeApplicantIndex, String rejectReason) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);

    franchiseeApplicantEntity.reject(rejectReason);
  }
}
