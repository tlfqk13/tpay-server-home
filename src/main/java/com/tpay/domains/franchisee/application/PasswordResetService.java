package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.PasswordResetRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

  private final FranchiseeRepository franchiseeRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public ResponseEntity resetPassword(PasswordResetRequest passwordResetRequest) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findByBusinessNumber(passwordResetRequest.getBusinessNumber())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Business Number"));

    franchiseeEntity.resetPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
    return ResponseEntity.ok().build();
  }
}
