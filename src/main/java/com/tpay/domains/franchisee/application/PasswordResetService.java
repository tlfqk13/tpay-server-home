package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.PasswordResetRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

  private final FranchiseeFindService franchiseeFindService;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public ResponseEntity reset(PasswordResetRequest passwordResetRequest) {
    FranchiseeEntity franchiseeEntity =
        franchiseeFindService.findByBusinessNumber(passwordResetRequest.getBusinessNumber());

    franchiseeEntity.resetPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
    return ResponseEntity.ok().build();
  }
}
