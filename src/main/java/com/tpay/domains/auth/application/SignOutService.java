package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final EmployeeTokenRepository employeeTokenRepository;
    private final UserPushTokenRepository userPushTokenRepository;
    private final FranchiseeFindService franchiseeFindService;

    @Transactional
    public String signOut(SignOutRequest signOutRequest) {
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
