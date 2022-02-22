package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.SignInSelector;
import com.tpay.domains.auth.application.dto.EmployeeTokenInfo;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SignInService {

  private final FranchiseeSignInService franchiseeSignInService;
  private final EmployeeSignInService employeeSignInService;

  @Transactional
  public SignInTokenResponse signIn(SignInRequest signInRequest) {
    SignInTokenResponse signInTokenResponse;
    if (signInRequest.getSignInSelector().equals(SignInSelector.FRANCHISEE)) {
      FranchiseeTokenInfo franchiseeTokenInfo = franchiseeSignInService.signIn(signInRequest.getBusinessNumber(), signInRequest.getPassword());
      signInTokenResponse = SignInTokenResponse.builder()
          .signUpDate(franchiseeTokenInfo.getSignUpDate())
          .franchiseeStatus(franchiseeTokenInfo.getFranchiseeStatus())
          .franchiseeIndex(franchiseeTokenInfo.getFranchiseeIndex())
          .businessNumber(franchiseeTokenInfo.getBusinessNumber())
          .rejectReason(franchiseeTokenInfo.getRejectReason())
          .popUp(franchiseeTokenInfo.isPopUp())
          .accessToken(franchiseeTokenInfo.getAccessToken())
          .refreshToken(franchiseeTokenInfo.getRefreshToken())
          .build();
    } else if (signInRequest.getSignInSelector().equals(SignInSelector.EMPLOYEE)) {
      EmployeeTokenInfo employeeTokenInfo = employeeSignInService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
      signInTokenResponse = SignInTokenResponse.builder()
          .employeeIndex(employeeTokenInfo.getEmployeeIndex())
          .userId(employeeTokenInfo.getUserId())
          .name(employeeTokenInfo.getName())
          .registeredDate(employeeTokenInfo.getRegisteredDate())
          .accessToken(employeeTokenInfo.getAccessToken())
          .refreshToken(employeeTokenInfo.getRefreshToken())
          .build();
    } else {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Parse Failed");
    }
    return signInTokenResponse;
  }

}
