package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfoInterface;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantFindService {

  private final FranchiseeApplicantRepository franchiseeApplicantRepository;

  public FranchiseeApplicantEntity findByIndex(Long franchiseeApplicantIndex) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantRepository
            .findById(franchiseeApplicantIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Applicant Index"));

    return franchiseeApplicantEntity;
  }

  public List<FranchiseeApplicantInfoInterface> findAll() {
    List<FranchiseeApplicantInfoInterface> franchiseeApplicantInfoInterfaceList = franchiseeApplicantRepository.findAllNativeQuery();
    return franchiseeApplicantInfoInterfaceList;
  }

  public FranchiseeApplicantEntity findByBusinessNumber(String businessNumber) {
    businessNumber = businessNumber.replaceAll("-", "");

    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantRepository
            .findByFranchiseeEntityBusinessNumber(businessNumber)
            .orElseThrow(
                () ->
                    new InvalidParameterException(
                        ExceptionState.INVALID_PARAMETER, "가입 내역이 존재하지 않습니다."));

    return franchiseeApplicantEntity;
  }
}
