package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantReapplyResponse;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeReApplyService {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;
  private final FranchiseeBankFindService franchiseeBankFindService;
  private final FranchiseeUploadFindService franchiseeUploadFindService;

  @Transactional
  public FranchiseeApplicantInfo reapply(String businessNumber) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantFindService.findByBusinessNumber(businessNumber);
    franchiseeApplicantEntity.reapply();
    return FranchiseeApplicantInfo.toResponse(franchiseeApplicantEntity);
  }


  @Transactional
  public FranchiseeApplicantReapplyResponse findBaseInfo(Long franchiseeApplicantIndex) {
    FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);
    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
    FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeEntity.getId());
    FranchiseeBankEntity franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
    return FranchiseeApplicantReapplyResponse.builder()
        .uploadImage(franchiseeUploadEntity.getS3Path())
        .franchiseeBankInfo(FranchiseeBankInfo.of(franchiseeBankEntity))
        .build();
  }
}
