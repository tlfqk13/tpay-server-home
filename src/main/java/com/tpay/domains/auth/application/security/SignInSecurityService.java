package com.tpay.domains.auth.application.security;

import com.tpay.domains.auth.application.EmployeeSignInService;
import com.tpay.domains.auth.application.dto.SignInDto;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Profile(value = "security")
public class SignInSecurityService {

    private final FranchiseeSignInSecurityService franchiseeSignInSecurityService;
    private final EmployeeSignInService employeeSignInService;

    @Transactional
    public SignInTokenInfo signIn(SignInDto.Request signInRequest) {
        if(null == signInRequest.getUserId() || signInRequest.getUserId().isBlank()){
            return franchiseeSignInSecurityService.signIn(signInRequest.getBusinessNumber(), signInRequest.getPassword(), signInRequest.getPushToken());
        } else {
            return employeeSignInService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
        }
    }
}
