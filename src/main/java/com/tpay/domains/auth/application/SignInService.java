package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.EmployeeTokenInfo;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FranchiseeSignInService franchiseeSignInService;
    private final EmployeeSignInService employeeSignInService;

    @Transactional
    public SignInTokenInfo signIn(SignInRequest signInRequest) {
        SignInTokenInfo signInTokenInfo;

        if (signInRequest.getUserSelector().equals(FRANCHISEE)) {
            FranchiseeTokenInfo franchiseeTokenInfo = franchiseeSignInService.signIn(signInRequest.getBusinessNumber(), signInRequest.getPassword(), signInRequest.getPushToken());
            signInTokenInfo = SignInTokenInfo.builder()
                .signUpDate(franchiseeTokenInfo.getSignUpDate())
                .franchiseeStatus(franchiseeTokenInfo.getFranchiseeStatus())
                .franchiseeIndex(franchiseeTokenInfo.getFranchiseeIndex())
                .businessNumber(franchiseeTokenInfo.getBusinessNumber())
                .rejectReason(franchiseeTokenInfo.getRejectReason())
                .popUp(franchiseeTokenInfo.isPopUp())
                .accessToken(franchiseeTokenInfo.getAccessToken())
                .refreshToken(franchiseeTokenInfo.getRefreshToken())
                .isActiveSound(franchiseeTokenInfo.isActiveSound())
                .isActiveVibration(franchiseeTokenInfo.isActiveVibration())
                .storeName(franchiseeTokenInfo.getStoreName())
                .isConnectedPos(franchiseeTokenInfo.isConnectedPos())
                .posType(franchiseeTokenInfo.getPosType())
                .userSelector(FRANCHISEE)
                .build();
        } else if (signInRequest.getUserSelector().equals(EMPLOYEE)) {
            EmployeeTokenInfo employeeTokenInfo = employeeSignInService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
            signInTokenInfo = SignInTokenInfo.builder()
                .employeeIndex(employeeTokenInfo.getEmployeeIndex())
                .userId(employeeTokenInfo.getUserId())
                .name(employeeTokenInfo.getName())
                .registeredDate(employeeTokenInfo.getRegisteredDate())
                .accessToken(employeeTokenInfo.getAccessToken())
                .refreshToken(employeeTokenInfo.getRefreshToken())
                .franchiseeIndex(employeeTokenInfo.getFranchiseeIndex())
                .franchiseeStatus(employeeTokenInfo.getFranchiseeStatus())
                .isActiveSound(employeeTokenInfo.isActiveSound())
                .isActiveVibration(employeeTokenInfo.isActiveVibration())
                .isConnectedPos(employeeTokenInfo.isConnectedPos())
                .userSelector(EMPLOYEE)
                .build();
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Parse Failed");
        }

//        logger.trace();
        return signInTokenInfo;
    }

}
