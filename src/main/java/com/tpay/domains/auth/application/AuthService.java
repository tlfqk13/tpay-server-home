package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.jwt.TokenType;
import com.tpay.domains.auth.domain.*;
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
    private final FranchiseeAccessTokenRepository franchiseeAccessTokenRepository;
    private final EmployeeAccessTokenRepository employeeAccessTokenRepository;

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

    @Transactional
    public void updateOrSaveAccessToken(FranchiseeEntity franchiseeEntity, String accessToken) {
        FranchiseeAccessTokenEntity franchiseeTokenEntity =
                franchiseeAccessTokenRepository.existsByFranchiseeEntityId(franchiseeEntity.getId())
                ? franchiseeAccessTokenRepository
                        .findByFranchiseeEntityId(franchiseeEntity.getId())
                        .get().accessToken(accessToken)
                : franchiseeAccessTokenRepository.save(
                        FranchiseeAccessTokenEntity.builder()
                                .franchiseeEntity(franchiseeEntity)
                                .accessToken(accessToken)
                                .build());
    }

    @Transactional
    public void updateOrSaveAccessToken(EmployeeEntity employeeEntity, String accessToken) {
        EmployeeAccessTokenEntity employeeAccessTokenEntity =
                employeeAccessTokenRepository.existsByEmployeeEntityId(employeeEntity.getId())
                        ? employeeAccessTokenRepository
                        .findByEmployeeEntityId(employeeEntity.getId())
                        .get().accessToken(accessToken)
                        : employeeAccessTokenRepository.save(
                        EmployeeAccessTokenEntity.builder()
                                .employeeEntity(employeeEntity)
                                .accessToken(accessToken)
                                .build());
    }

    public AuthToken createAccessToken(FranchiseeEntity franchiseeEntity) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!_1");
        Map<String, Object> payload = new LinkedHashMap<>();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!_2");
        payload.put("accessF", franchiseeEntity.getId());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!_3");
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
