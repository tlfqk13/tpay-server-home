package com.tpay.domains.auth.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Map;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final EmployeeTokenRepository employeeTokenRepository;
    private final UserPushTokenRepository userPushTokenRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final EmployeeFindService employeeFindService;

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

    @Transactional
    public String signOutNew() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String bizNum = auth.getName();

        String userid = null;
        if(null != auth.getDetails()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> details = objectMapper.convertValue(auth.getDetails(), Map.class);
            userid = details.get("userid");
        }

        if (null == userid) {
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByBusinessNumber(bizNum);
            Long id = franchiseeEntity.getId();
            franchiseeTokenRepository.deleteByFranchiseeEntityId(id);
            userPushTokenRepository.deleteByFranchiseeEntity(franchiseeEntity);

            return "FRANCHISEE Log out";
        } else if (!userid.isEmpty()) {
            EmployeeEntity employeeEntity = employeeFindService.findByUserId(userid);
            employeeTokenRepository.deleteByEmployeeEntityId(employeeEntity.getId());
            return "EMPLOYEE Log out";
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Parameter(FRANCHISEE or EMPLOYEE)");
        }
    }

}
