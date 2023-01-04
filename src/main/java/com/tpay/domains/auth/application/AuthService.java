package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.jwt.TokenType;
import com.tpay.domains.auth.domain.*;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * refresh token과 refresh token을 관리하는 테이블이 다르고
 * 연관관계를 각 토큰의 테이블에서 관리하기에 점주나 직원의 엔티티로 토큰을 관리하는 복잡성이 올라감
 * 향후 토큰관리 테이블을 하나로 합치고 연관관계 재정립이 필요할 것으로 사료됨
 */
@Service
@RequiredArgsConstructor
@Slf4j
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

    // TODO: 2023/01/04 샘플라스 문제
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
