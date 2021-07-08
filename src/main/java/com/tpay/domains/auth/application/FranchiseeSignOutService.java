package com.tpay.domains.auth.application;

import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeSignOutService {

  private final FranchiseeTokenRepository franchiseeTokenRepository;

  @Transactional
  public void signOut(FranchiseeTokenInfo franchiseeTokenInfo) {
    Long franchiseeIndex = franchiseeTokenInfo.getFranchiseeIndex();
    franchiseeTokenRepository.deleteByFranchiseeEntityId(franchiseeIndex);
  }
}
