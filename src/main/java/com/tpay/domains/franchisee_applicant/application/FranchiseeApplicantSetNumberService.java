package com.tpay.domains.franchisee_applicant.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantSetNumberRequest;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantSetNumberService {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;
  private final FranchiseeUploadFindService franchiseeUploadFindService;

  @Transactional
  public String updateTaxFreeStoreNumber(Long franchiseeApplicantIndex, FranchiseeApplicantSetNumberRequest franchiseeApplicantSetNumberRequest) {
    FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);
    FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeApplicantEntity.getFranchiseeEntity().getId());
    if(franchiseeApplicantSetNumberRequest.getTaxFreeStoreNumber().length() != 8){
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "'Tax Free Store Number' is 8 length digits");
    }
    return franchiseeUploadEntity.updateTaxFreeStoreNumber(franchiseeApplicantSetNumberRequest.getTaxFreeStoreNumber());
  }
}

