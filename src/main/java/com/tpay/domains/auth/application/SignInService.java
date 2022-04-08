package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.EmployeeTokenInfo;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final FranchiseeSignInService franchiseeSignInService;
    private final EmployeeSignInService employeeSignInService;

    private final UserPushTokenService userPushTokenService;

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
                .userSelector(FRANCHISEE)
                .build();
        } else if (signInRequest.getUserSelector().equals(EMPLOYEE)) {
            EmployeeTokenInfo employeeTokenInfo = employeeSignInService.signIn(signInRequest.getUserId(), signInRequest.getPassword(), signInRequest.getPushToken());
            signInTokenInfo = SignInTokenInfo.builder()
                .employeeIndex(employeeTokenInfo.getEmployeeIndex())
                .userId(employeeTokenInfo.getUserId())
                .name(employeeTokenInfo.getName())
                .registeredDate(employeeTokenInfo.getRegisteredDate())
                .accessToken(employeeTokenInfo.getAccessToken())
                .refreshToken(employeeTokenInfo.getRefreshToken())
                .franchiseeIndex(employeeTokenInfo.getFranchiseeIndex())
                .franchiseeStatus(employeeTokenInfo.getFranchiseeStatus())
                .userSelector(EMPLOYEE)
                .build();
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Parse Failed");
        }


        Long userId = signInRequest.getUserSelector() == FRANCHISEE ? signInTokenInfo.getFranchiseeIndex() : signInTokenInfo.getEmployeeIndex();

        UserPushTokenEntity userPushTokenEntity = UserPushTokenEntity.builder()
                .userType(signInRequest.getUserSelector())
                .userId(userId.toString())
                .userToken(signInRequest.getPushToken())
                .build();

        userPushTokenService.save(userPushTokenEntity);



        return signInTokenInfo;
    }

}
