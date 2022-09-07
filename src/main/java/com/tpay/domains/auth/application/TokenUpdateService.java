package com.tpay.domains.auth.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.auth.domain.EmployeeTokenEntity;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenUpdateService {
    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final EmployeeTokenRepository employeeTokenRepository;
    private final EmployeeFindService employeeFindService;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @Transactional
    public SignInTokenInfo refresh(SignInTokenInfo signInTokenInfo) {
        AuthToken refreshToken = jwtUtils.convertAuthToken(signInTokenInfo.getRefreshToken());
        Long parsedIndex;

        if (signInTokenInfo.getUserSelector().equals(FRANCHISEE)) {
            FranchiseeTokenEntity franchiseeTokenEntity =
                    franchiseeTokenRepository
                            .findByFranchiseeEntityId(signInTokenInfo.getFranchiseeIndex())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee"));

            try {
                //parsedIndex = Long.parseLong(String.valueOf(refreshToken.getData()));
                parsedIndex = signInTokenInfo.getFranchiseeIndex();
            } catch (Exception exception) {
                throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
            }
            franchiseeTokenEntity.validUser(parsedIndex);
            franchiseeTokenEntity.validToken(refreshToken.getValue());

            AuthToken accessToken =
                    authService.createAccessToken(franchiseeTokenEntity.getFranchiseeEntity());
            FranchiseeEntity franchiseeEntity = franchiseeTokenEntity.getFranchiseeEntity();
            authService.updateOrSaveAccessToken(franchiseeEntity,accessToken.getValue());
            return SignInTokenInfo.builder()
                    .signUpDate(signInTokenInfo.getSignUpDate())
                    .franchiseeStatus(signInTokenInfo.getFranchiseeStatus())
                    .franchiseeIndex(signInTokenInfo.getFranchiseeIndex())
                    .businessNumber(signInTokenInfo.getBusinessNumber())
                    .rejectReason(signInTokenInfo.getRejectReason())
                    .popUp(signInTokenInfo.isPopUp())
                    .accessToken(accessToken.getValue())
                    .refreshToken(signInTokenInfo.getRefreshToken())
                    .userSelector(FRANCHISEE)
                    .build();
        } else if (signInTokenInfo.getUserSelector().equals(EMPLOYEE)) {
            EmployeeEntity employeeEntity = employeeFindService.findById(signInTokenInfo.getEmployeeIndex())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Employee"));
            EmployeeTokenEntity employeeTokenEntity = employeeTokenRepository.findByEmployeeEntity(employeeEntity)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Employee Entity"));

            try {
                parsedIndex = Long.parseLong(String.valueOf(refreshToken.getData().get("employeeIndexJwt")));
            } catch (Exception e) {
                throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
            }

            employeeTokenEntity.validUser(parsedIndex);
            employeeTokenEntity.validToken(refreshToken.getValue());

            AuthToken accessToken = authService.createAccessToken(employeeEntity);

            return SignInTokenInfo.builder()
                    .employeeIndex(signInTokenInfo.getEmployeeIndex())
                    .userId(signInTokenInfo.getUserId())
                    .name(signInTokenInfo.getName())
                    .registeredDate(signInTokenInfo.getRegisteredDate())
                    .accessToken(accessToken.getValue())
                    .refreshToken(signInTokenInfo.getRefreshToken())
                    .userSelector(EMPLOYEE)
                    .build();

        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Parse Failed");
        }
    }
}
