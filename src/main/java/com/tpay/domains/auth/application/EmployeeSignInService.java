package com.tpay.domains.auth.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.NonBatchPushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
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

        checkPassword(password, employeeEntity);
        checkDeleteEmployee(employeeEntity);

        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByFranchiseeEntity(employeeEntity.getFranchiseeEntity());
        AuthToken accessToken = authService.createAccessToken(employeeEntity);
        AuthToken refreshToken = authService.createRefreshToken(employeeEntity);
        authService.updateOrSaveRefreshToken(employeeEntity, refreshToken.getValue());
        log.trace(" @@ employeeEntity.getId updateOrSaveAccessToken_start = {}", employeeEntity.getId());
        authService.updateOrSaveAccessToken(employeeEntity, accessToken.getValue());
        log.trace(" @@ employeeEntity.getId updateOrSaveAccessToken_end = {}", employeeEntity.getId());

        nonBatchPushService.nonBatchPushNSave(PushCategoryType.CASE_FOURTEEN, franchiseeApplicantEntity.getFranchiseeEntity().getId(), employeeEntity.getName());

        log.trace("==========================로그인===========================");
        log.trace("[사업자번호] : {}", employeeEntity.getName());
        log.trace("[사업자번호] : {}", employeeEntity.getFranchiseeEntity().getBusinessNumber());
        log.trace("==========================로그인===========================");

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
                .userSelector(UserSelector.EMPLOYEE)
                .build();
    }

    private void checkDeleteEmployee(EmployeeEntity employeeEntity) {
        if (employeeEntity.getIsDelete()) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "가입 내역이 존재하지 않습니다. 다시 입력해주세요.");
        }
    }

    private void checkPassword(String password, EmployeeEntity employeeEntity) {
        if (!passwordEncoder.matches(password, employeeEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid Password");
        }
    }
}
