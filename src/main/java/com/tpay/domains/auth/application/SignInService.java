package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final FranchiseeSignInService franchiseeSignInService;
    private final EmployeeSignInService employeeSignInService;
    private final AccessTokenService accessTokenService;

    @Transactional
    public SignInTokenInfo signIn(SignInRequest signInRequest) {
        if (!signInRequest.isForcedCheck()) {
            if (duplicateSignIn(signInRequest)) {
                throw new InvalidParameterException(ExceptionState.DUPLICATE_SIGNIN, "중복 로그인입니다");
            }
        }
        return signInProc(signInRequest);
    }

    private SignInTokenInfo signInProc(SignInRequest signInRequest) {
        if (signInRequest.getUserSelector().equals(FRANCHISEE)) {
            return franchiseeSignInService.signIn(signInRequest.getBusinessNumber(), signInRequest.getPassword(), signInRequest.getPushToken());
        } else if (signInRequest.getUserSelector().equals(EMPLOYEE)) {
            return employeeSignInService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Parse Failed");
        }
    }

    @Transactional(readOnly = true)
    public boolean duplicateSignIn(SignInRequest signInRequest) {
        if (signInRequest.getUserSelector().equals(FRANCHISEE)) {
            String businessNumber = signInRequest.getBusinessNumber();
            businessNumber = businessNumber.replaceAll("-", "");
            return accessTokenService.existByBusinessNumber(businessNumber);
        } else {
            return accessTokenService.existByUserId(signInRequest.getUserId());
        }
    }
}
