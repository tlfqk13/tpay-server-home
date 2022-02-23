package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.util.SignInSelector.EMPLOYEE;
import static com.tpay.commons.util.SignInSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignOutService {

  private final FranchiseeTokenRepository franchiseeTokenRepository;
  private final EmployeeTokenRepository employeeTokenRepository;

  @Transactional
  public String signOut(SignOutRequest signOutRequest) {
    if (signOutRequest.getSignInSelector().equals(FRANCHISEE) && signOutRequest.getFranchiseeIndex() != null) {
      franchiseeTokenRepository.deleteByFranchiseeEntityId(signOutRequest.getFranchiseeIndex());
      return "FRANCHISEE Log out";
    } else if (signOutRequest.getSignInSelector().equals(EMPLOYEE) && signOutRequest.getEmployeeIndex() != null) {
      employeeTokenRepository.deleteByEmployeeEntity_Id(signOutRequest.getEmployeeIndex());
      return "EMPLOYEE Log out";
    } else {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Parameter(FRANCHISEE or EMPLOYEE)");
    }
  }

}
