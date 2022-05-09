package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.jwt.TokenType;
import com.tpay.domains.auth.domain.EmployeeTokenEntity;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final EmployeeTokenRepository employeeTokenRepository;

    @Transactional
    public void updateOrSave(FranchiseeEntity franchiseeEntity, String refreshToken) {
        FranchiseeTokenEntity franchiseeTokenEntity =
            franchiseeTokenRepository.existsByFranchiseeEntityId(franchiseeEntity.getId())
                ? franchiseeTokenRepository
                .findByFranchiseeEntityId(franchiseeEntity.getId())
                .get()
                .refreshToken(refreshToken)
                : franchiseeTokenRepository.save(
                FranchiseeTokenEntity.builder()
                    .franchiseeEntity(franchiseeEntity)
                    .refreshToken(refreshToken)
                    .build());
    }

    @Transactional
    public void updateOrSave(EmployeeEntity employeeEntity, String refreshToken) {
        EmployeeTokenEntity employeeTokenEntity =
            employeeTokenRepository.existsByEmployeeEntity(employeeEntity)
                ? employeeTokenRepository.findByEmployeeEntity(employeeEntity)
                .get()
                .refreshToken(refreshToken)
                : employeeTokenRepository.save(
                EmployeeTokenEntity.builder()
                    .employeeEntity(employeeEntity)
                    .refreshToken(refreshToken)
                    .build());

    }

    public AuthToken createAccessToken(FranchiseeEntity franchiseeEntity) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("accessF", franchiseeEntity.getId());
        return jwtUtils.createAuthToken(payload, TokenType.ACCESS_TOKEN);
    }

    public AuthToken createAccessToken(EmployeeEntity employeeEntity) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("accessE", employeeEntity.getId());
        return jwtUtils.createAuthToken(payload, TokenType.ACCESS_TOKEN);
    }

    public AuthToken createRefreshToken(FranchiseeEntity franchiseeEntity) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("refreshF", franchiseeEntity.getId());
        return jwtUtils.createAuthToken(payload, TokenType.REFRESH_TOKEN);
    }

    public AuthToken createRefreshToken(EmployeeEntity employeeEntity) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("refreshE", employeeEntity.getId());
        return jwtUtils.createAuthToken(payload, TokenType.REFRESH_TOKEN);
    }
}
