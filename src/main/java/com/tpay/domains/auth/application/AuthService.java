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
import java.util.LinkedHashMap;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
  public void updateOrSave(EmployeeEntity employeeEntity, String refreshToken){
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
    payload.put("businessNumber", franchiseeEntity.getBusinessNumber());
    return jwtUtils.createAuthToken(payload, TokenType.ACCESS_TOKEN);
  }

  public AuthToken createAccessToken(EmployeeEntity employeeEntity){
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("userId", employeeEntity.getUserId());
    return jwtUtils.createAuthToken(payload,TokenType.ACCESS_TOKEN);
  }

  public AuthToken createRefreshToken(FranchiseeEntity franchiseeEntity) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("franchiseeIndex", franchiseeEntity.getId());
    return jwtUtils.createAuthToken(payload, TokenType.REFRESH_TOKEN);
  }

  public AuthToken createRefreshToken(EmployeeEntity employeeEntity){
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("employeeIndexJwt", employeeEntity.getId());
    return jwtUtils.createAuthToken(payload, TokenType.REFRESH_TOKEN);
  }
}
