package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

  public List<FranchiseeApplicantInfo> findAll() {
    List<FranchiseeApplicantInfo> franchiseeApplicantInfoList =
        franchiseeApplicantRepository.findAll().stream()
            .map(
                franchiseeApplicantEntity ->
                    FranchiseeApplicantInfo.toResponse(franchiseeApplicantEntity))
            .collect(Collectors.toList());
    return franchiseeApplicantInfoList;
  }

  public FranchiseeApplicantInfo find(Long franchiseeApplicantIndex) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        this.findByIndex(franchiseeApplicantIndex);

    return FranchiseeApplicantInfo.toResponse(franchiseeApplicantEntity);
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
