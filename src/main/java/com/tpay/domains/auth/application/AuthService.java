package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.jwt.TokenType;
import com.tpay.domains.auth.domain.FranchiseeTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
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

  @Transactional
  public void save(FranchiseeEntity franchiseeEntity, String refreshToken) {
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

  public AuthToken createAccessToken(FranchiseeEntity franchiseeEntity) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("businessNumber", franchiseeEntity.getBusinessNumber());
    return jwtUtils.createAuthToken(payload, TokenType.ACCESS_TOKEN);
  }

  public AuthToken createRefreshToken(FranchiseeEntity franchiseeEntity) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("franchiseeIndex", franchiseeEntity.getId());
    return jwtUtils.createAuthToken(payload, TokenType.REFRESH_TOKEN);
  }
}
