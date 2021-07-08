package com.tpay.domains.auth.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.domain.FranchiseeTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUpdateService {
  private final FranchiseeTokenRepository franchiseeTokenRepository;
  private final JwtUtils jwtUtils;
  private final AuthService authService;

  @Transactional
  public FranchiseeTokenInfo refresh(FranchiseeTokenInfo franchiseeTokenInfo) {
    AuthToken refreshToken = jwtUtils.convertAuthToken(franchiseeTokenInfo.getRefreshToken());
    FranchiseeTokenEntity franchiseeTokenEntity =
        franchiseeTokenRepository
            .findByFranchiseeEntityId(franchiseeTokenInfo.getFranchiseeIndex())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee"));

    Long parsedIndex = -1L;
    try {
      parsedIndex = Long.parseLong(String.valueOf(refreshToken.getData().get("franchiseeIndex")));
    } catch (Exception exception) {
      throw new JwtRuntimeException(ExceptionState.FORCE_REFRESH);
    }
    franchiseeTokenEntity.validUser(parsedIndex);
    franchiseeTokenEntity.validToken(refreshToken.getValue());

    AuthToken accessToken =
        authService.createAccessToken(franchiseeTokenEntity.getFranchiseeEntity());
    return FranchiseeTokenInfo.builder()
        .franchiseeIndex(parsedIndex)
        .accessToken(accessToken.getValue())
        .refreshToken(refreshToken.getValue())
        .build();
  }
}
