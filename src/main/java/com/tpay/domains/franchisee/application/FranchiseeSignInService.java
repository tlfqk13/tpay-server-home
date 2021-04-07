package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.FranchiseeSignInRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignInResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeSignInService {

  private final FranchiseeRepository franchiseeRepository;
  private final PasswordEncoder passwordEncoder;

  public ResponseEntity<FranchiseeSignInResponse> signIn(
      FranchiseeSignInRequest franchiseeSignInRequest) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findByBusinessNumber(franchiseeSignInRequest.getBusinessNumber())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Business Number"));

    if (!passwordEncoder.matches(
        franchiseeSignInRequest.getPassword(), franchiseeEntity.getPassword())) {
      throw new IllegalArgumentException("Invalid Password");
    }

    return ResponseEntity.ok(
        FranchiseeSignInResponse.builder().id(franchiseeEntity.getId()).build());
  }
}
