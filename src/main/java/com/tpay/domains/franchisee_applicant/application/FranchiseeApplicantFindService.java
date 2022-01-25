package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfoInterface;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
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

  public FranchiseeApplicantEntity findByFranchiseeEntity(FranchiseeEntity franchiseeEntity) {
    FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantRepository.findByFranchiseeEntity(franchiseeEntity)
        .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Entity"));
    return franchiseeApplicantEntity;
  }

  public List<FranchiseeApplicantInfoInterface> findAll() {
    return franchiseeApplicantRepository.findAllNativeQuery();
  }

  public List<FranchiseeApplicantInfoInterface> filterIsRead(String value) {
    boolean valueBoolean;
    String filter;
    try {
      valueBoolean = Boolean.parseBoolean(value);
    } catch (Exception e) {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "value must be true/false");
    }
    if (valueBoolean) {
      filter = "1";
    } else {
      filter = "0";
    }
    return franchiseeApplicantRepository.filterIsReadNativeQuery(filter);
  }

  public List<FranchiseeApplicantInfoInterface> filterFranchiseeStatus(FranchiseeStatus value) {
    Integer ordinal = value.ordinal();
    String filter = ordinal.toString();
    return franchiseeApplicantRepository.filterFranchiseeStatusNativeQuery(filter);
  }

  public List<FranchiseeApplicantInfoInterface> filterBoth(FranchiseeStatus value) {
    Integer ordinal = value.ordinal();
    String statusFilter = ordinal.toString();
    return franchiseeApplicantRepository.filterBothNativeQuery(statusFilter);

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
