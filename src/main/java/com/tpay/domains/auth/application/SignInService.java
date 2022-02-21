package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.SignInSelector;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.presentation.SignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SignInService {

  private final FranchiseeSignInService franchiseeSignInService;


  @Transactional
  public FranchiseeTokenInfo signIn(SignInRequest signInRequest) {
    if(signInRequest.getSignInSelector().equals(SignInSelector.FRANCHISEE)){
      FranchiseeTokenInfo franchiseeTokenInfo = franchiseeSignInService.signIn(signInRequest.getBusinessNumber(), signInRequest.getPassword());
      return franchiseeTokenInfo;
    }
    else { throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Dd");}
  }
}
