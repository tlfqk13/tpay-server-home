package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
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
  public ResponseEntity reset(PasswordResetRequest request) {
    FranchiseeEntity franchiseeEntity =
        franchiseeFindService.findByBusinessNumber(request.getBusinessNumber());

    if (!franchiseeEntity.isValidUser(request.getName(), request.getPhoneNumber())) {
      throw new InvalidParameterException(
          ExceptionState.INVALID_PARAMETER,
          "Mismatch between BusinessNumber and Certification Info");
    }

    franchiseeEntity.resetPassword(passwordEncoder.encode(request.getNewPassword()));
    return ResponseEntity.ok().build();
  }
}
