package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.EmployeeTokenInfo;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.util.SignInSelector.EMPLOYEE;
import static com.tpay.commons.util.SignInSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignInService {

  private final FranchiseeSignInService franchiseeSignInService;
  private final EmployeeSignInService employeeSignInService;

  @Transactional
  public SignInTokenInfo signIn(SignInRequest signInRequest) {
    SignInTokenInfo signInTokenInfo;
    if (signInRequest.getSignInSelector().equals(FRANCHISEE)) {
      FranchiseeTokenInfo franchiseeTokenInfo = franchiseeSignInService.signIn(signInRequest.getBusinessNumber(), signInRequest.getPassword());
      signInTokenInfo = SignInTokenInfo.builder()
          .signUpDate(franchiseeTokenInfo.getSignUpDate())
          .franchiseeStatus(franchiseeTokenInfo.getFranchiseeStatus())
          .franchiseeIndex(franchiseeTokenInfo.getFranchiseeIndex())
          .businessNumber(franchiseeTokenInfo.getBusinessNumber())
          .rejectReason(franchiseeTokenInfo.getRejectReason())
          .popUp(franchiseeTokenInfo.isPopUp())
          .accessToken(franchiseeTokenInfo.getAccessToken())
          .refreshToken(franchiseeTokenInfo.getRefreshToken())
          .signInSelector(FRANCHISEE)
          .build();
    } else if (signInRequest.getSignInSelector().equals(EMPLOYEE)) {
      EmployeeTokenInfo employeeTokenInfo = employeeSignInService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
      signInTokenInfo = SignInTokenInfo.builder()
          .employeeIndex(employeeTokenInfo.getEmployeeIndex())
          .userId(employeeTokenInfo.getUserId())
          .name(employeeTokenInfo.getName())
          .registeredDate(employeeTokenInfo.getRegisteredDate())
          .accessToken(employeeTokenInfo.getAccessToken())
          .refreshToken(employeeTokenInfo.getRefreshToken())
          .signInSelector(EMPLOYEE)
          .build();
    } else {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Parse Failed");
    }
    return signInTokenInfo;
  }

}
