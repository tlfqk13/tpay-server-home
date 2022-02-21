package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.domains.auth.application.dto.FranchiseeSignInRequest;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeSignInService {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public FranchiseeTokenInfo signIn(String businessNumber, String password) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantFindService.findByBusinessNumber(businessNumber);

    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

    if (!passwordEncoder.matches(
        password, franchiseeEntity.getPassword())) {
      throw new IllegalArgumentException("Invalid Password");
    }

    AuthToken accessToken = authService.createAccessToken(franchiseeEntity);
    AuthToken refreshToken = authService.createRefreshToken(franchiseeEntity);
    authService.save(franchiseeEntity, refreshToken.getValue());

    return FranchiseeTokenInfo.builder()
        .franchiseeIndex(franchiseeEntity.getId())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
        .rejectReason(franchiseeApplicantEntity.getRejectReason())
        .accessToken(accessToken.getValue())
        .refreshToken(refreshToken.getValue())
        .popUp(franchiseeEntity.isPopUp())
        .signUpDate(franchiseeEntity.getCreatedDate())
        .build();
  }
}
