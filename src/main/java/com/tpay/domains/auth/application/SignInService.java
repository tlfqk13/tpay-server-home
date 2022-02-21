package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.SignInSelector;
import com.tpay.domains.auth.application.dto.EmployeeTokenInfo;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.presentation.SignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SignInService {

  private final FranchiseeSignInService franchiseeSignInService;
  private final EmployeeSignInService employeeSignInService;


  @Transactional
  public Object signIn(SignInRequest signInRequest) {
    if (signInRequest.getSignInSelector().equals(SignInSelector.FRANCHISEE)) {
      FranchiseeTokenInfo franchiseeTokenInfo = franchiseeSignInService.signIn(signInRequest.getBusinessNumber(), signInRequest.getPassword());
      return franchiseeTokenInfo;
    } else if (signInRequest.getSignInSelector().equals(SignInSelector.EMPLOYEE)) {
      EmployeeTokenInfo employeeTokenInfo = employeeSignInService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
      return employeeTokenInfo;
    } else {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Parse Failed");
    }
  }

}
