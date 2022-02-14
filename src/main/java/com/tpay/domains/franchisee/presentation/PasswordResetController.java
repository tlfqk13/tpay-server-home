package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.PasswordResetService;
import com.tpay.domains.franchisee.application.dto.PasswordChangeRequest;
import com.tpay.domains.franchisee.application.dto.PasswordCorrectRequest;
import com.tpay.domains.franchisee.application.dto.PasswordResetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @GetMapping("/franchisee/password/{businessNumber}")
  public ResponseEntity<Boolean> existBusinessNumber(@PathVariable String businessNumber) {
    boolean result = passwordResetService.existBusinessNumber(businessNumber);
    return ResponseEntity.ok(result);
  }


  @PatchMapping("/franchisee/password/{businessNumber}")
  public ResponseEntity<Boolean> resetPasswordOut(
      @PathVariable String businessNumber,
      @RequestBody PasswordResetRequest passwordResetRequest) {
    boolean result = passwordResetService.reset(businessNumber, passwordResetRequest);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/franchisee/password/in/{franchiseeIndex}")
  public ResponseEntity<Boolean> correctPassword(
      @PathVariable Long franchiseeIndex,
      @RequestBody PasswordCorrectRequest passwordCorrectRequest
  ) {
    boolean result = passwordResetService.correctPassword(franchiseeIndex, passwordCorrectRequest);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/franchisee/password/in/{franchiseeIndex}")
  public ResponseEntity<Boolean> resetPasswordIn(
      @PathVariable Long franchiseeIndex,
      @RequestBody PasswordChangeRequest passwordChangeRequest) {
    boolean result = passwordResetService.change(franchiseeIndex, passwordChangeRequest);
    return ResponseEntity.ok(result);
  }
}
