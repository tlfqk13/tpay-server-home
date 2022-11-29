package com.tpay.domains.auth.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.IndexInfo;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignOutService {

    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final EmployeeTokenRepository employeeTokenRepository;
    private final UserPushTokenRepository userPushTokenRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final AccessTokenService accessTokenService;

    @Transactional
    public void signOut(IndexInfo indexInfo) {
        Long index = indexInfo.getIndex();
        if (FRANCHISEE == indexInfo.getUserSelector()) {
            franchiseeTokenRepository.deleteByFranchiseeEntityId(index);
            FranchiseeEntity franchisee = franchiseeFindService.findByIndex(index);
            userPushTokenRepository.deleteByFranchiseeEntity(franchisee);
        } else {
            employeeTokenRepository.deleteByEmployeeEntityId(index);
        }
        accessTokenService.deleteById(indexInfo);

        log.debug("Log out UserType = {}, id = {}", indexInfo.getUserSelector(), index);
    }

    @Transactional
    public String duplicateSignOut(SignOutRequest signOutRequest) {
        if (signOutRequest.getUserSelector().equals(FRANCHISEE) && signOutRequest.getFranchiseeIndex() != null) {
            //푸시토큰 삭제
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(signOutRequest.getFranchiseeIndex());
            log.trace("==========================중복 강제 로그아웃===========================");
            log.trace("[사업자번호] : {}", franchiseeEntity.getBusinessNumber());
            log.trace("==========================중복 강제 로그아웃===========================");
            return "FRANCHISEE Log out";
        } else if (signOutRequest.getUserSelector().equals(EMPLOYEE) && signOutRequest.getEmployeeIndex() != null) {
            log.trace("==========================중복 강제 로그아웃===========================");
            log.trace("[직원번호] : {}", signOutRequest.getEmployeeIndex());
            log.trace("==========================중복 강제 로그아웃===========================");
            return "EMPLOYEE Log out";
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Parameter(FRANCHISEE or EMPLOYEE)");
        }
    }
}
