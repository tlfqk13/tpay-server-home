package com.tpay.domains.auth.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.NonBatchPushService;
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

    private final NonBatchPushService nonBatchPushService;

    @Transactional
    public SignInTokenInfo signIn(String userId, String password) {
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

        //직원 로그인시 푸쉬
        nonBatchPushService.nonBatchPushNSave(PushCategoryType.CASE_FOURTEEN, franchiseeApplicantEntity.getFranchiseeEntity().getId(), employeeEntity.getName());

        return SignInTokenInfo.builder()
            .employeeIndex(employeeEntity.getId())
            .userId(employeeEntity.getUserId())
            .name(employeeEntity.getName())
            .accessToken(accessToken.getValue())
            .refreshToken(refreshToken.getValue())
            .registeredDate(employeeEntity.getCreatedDate())
            .franchiseeIndex(employeeEntity.getFranchiseeEntity().getId())
            .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
            .isActiveSound(employeeEntity.getIsActiveSound())
            .isActiveVibration(employeeEntity.getIsActiveVibration())
            .isConnectedPos(employeeEntity.getFranchiseeEntity().getIsConnectedPos())
            .build();
    }
}
