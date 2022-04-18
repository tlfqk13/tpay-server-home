package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
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

    @Transactional
    public String signOut(SignOutRequest signOutRequest) {
        if (signOutRequest.getUserSelector().equals(FRANCHISEE) && signOutRequest.getFranchiseeIndex() != null) {
            franchiseeTokenRepository.deleteByFranchiseeEntityId(signOutRequest.getFranchiseeIndex());
            userPushTokenRepository.deleteByUserIdAndUserSelector(signOutRequest.getFranchiseeIndex().toString(), FRANCHISEE);
            return "FRANCHISEE Log out";
        } else if (signOutRequest.getUserSelector().equals(EMPLOYEE) && signOutRequest.getEmployeeIndex() != null) {
            employeeTokenRepository.deleteByEmployeeEntity_Id(signOutRequest.getEmployeeIndex());
            userPushTokenRepository.deleteByUserIdAndUserSelector(signOutRequest.getEmployeeIndex().toString(), EMPLOYEE);
            return "EMPLOYEE Log out";
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Parameter(FRANCHISEE or EMPLOYEE)");
        }
    }

}
