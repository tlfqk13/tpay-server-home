package com.tpay.domains.auth.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.domains.auth.application.dto.EmployeeTokenInfo;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeSignInService {

    private final EmployeeFindService employeeFindService;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;


    @Transactional
    public EmployeeTokenInfo signIn(String userId, String password) {
        EmployeeEntity employeeEntity = employeeFindService.findByUserId(userId);
        if (!passwordEncoder.matches(password, employeeEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid Password");
        }
        if (employeeEntity.getIsDelete()) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "가입 내역이 존재하지 않습니다. 다시 입력해주세요.");
        }

        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByFranchiseeEntity(employeeEntity.getFranchiseeEntity());
        AuthToken accessToken = authService.createAccessToken(employeeEntity);
        AuthToken refreshToken = authService.createRefreshToken(employeeEntity);
        authService.updateOrSave(employeeEntity, refreshToken.getValue());

        return EmployeeTokenInfo.builder()
            .employeeIndex(employeeEntity.getId())
            .userId(employeeEntity.getUserId())
            .name(employeeEntity.getName())
            .accessToken(accessToken.getValue())
            .refreshToken(refreshToken.getValue())
            .registeredDate(employeeEntity.getCreatedDate())
            .franchiseeIndex(employeeEntity.getFranchiseeEntity().getId())
            .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
            .build();


    }
}
