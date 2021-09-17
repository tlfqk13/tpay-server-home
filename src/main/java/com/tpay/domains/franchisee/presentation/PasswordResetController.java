package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.PasswordResetService;
import com.tpay.domains.franchisee.application.dto.PasswordResetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @PatchMapping("/franchisee/password")
  public ResponseEntity resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
    passwordResetService.reset(passwordResetRequest);
    return ResponseEntity.ok().build();
  }
}
