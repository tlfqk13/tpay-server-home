package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import com.tpay.domains.auth.domain.*;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final EmployeeTokenRepository employeeTokenRepository;
    private final UserPushTokenRepository userPushTokenRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final AccessTokenService accessTokenService;
    private final FranchiseeAccessTokenRepository franchiseeAccessTokenRepository;

    @Transactional
    public String signOut(SignOutRequest signOutRequest) {
        if (signOutRequest.getUserSelector().equals(FRANCHISEE) && signOutRequest.getFranchiseeIndex() != null) {
            franchiseeTokenRepository.deleteByFranchiseeEntityId(signOutRequest.getFranchiseeIndex());
            //푸시토큰 삭제
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(signOutRequest.getFranchiseeIndex());
            userPushTokenRepository.deleteByFranchiseeEntity(franchiseeEntity);
            accessTokenService.deleteByFranchiseeEntityId(signOutRequest.getFranchiseeIndex());
            return "FRANCHISEE Log out";
        } else if (signOutRequest.getUserSelector().equals(EMPLOYEE) && signOutRequest.getEmployeeIndex() != null) {
            employeeTokenRepository.deleteByEmployeeEntityId(signOutRequest.getEmployeeIndex());
            accessTokenService.deleteByEmployeeEntityId(signOutRequest.getEmployeeIndex());
            return "EMPLOYEE Log out";
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Parameter(FRANCHISEE or EMPLOYEE)");
        }
    }

    @Transactional
    public String duplicateSignOut(SignOutRequest signOutRequest) {
        if (signOutRequest.getUserSelector().equals(FRANCHISEE) && signOutRequest.getFranchiseeIndex() != null) {
            franchiseeTokenRepository.deleteByFranchiseeEntityId(signOutRequest.getFranchiseeIndex());
            //푸시토큰 삭제
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(signOutRequest.getFranchiseeIndex());
            userPushTokenRepository.deleteByFranchiseeEntity(franchiseeEntity);
            return "FRANCHISEE Log out";
        } else if (signOutRequest.getUserSelector().equals(EMPLOYEE) && signOutRequest.getEmployeeIndex() != null) {
            employeeTokenRepository.deleteByEmployeeEntityId(signOutRequest.getEmployeeIndex());
            return "EMPLOYEE Log out";
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Parameter(FRANCHISEE or EMPLOYEE)");
        }
    }
}
