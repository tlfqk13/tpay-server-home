package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeFindService {
  private final FranchiseeRepository franchiseeRepository;

  public FranchiseeEntity findByBusinessNumber(String businessNumber) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findByBusinessNumber(businessNumber.replaceAll("-", ""))
            .orElseThrow(
                () ->
                    new InvalidParameterException(
                        ExceptionState.INVALID_PARAMETER, "Invalid Business Number"));

    return franchiseeEntity;
  }

  public FranchiseeEntity findByIndex(Long franchiseeIndex) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findById(franchiseeIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee ID"));

    return franchiseeEntity;
  }
}
