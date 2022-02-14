package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.PasswordResetService;
import com.tpay.domains.franchisee.application.dto.PasswordChangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @GetMapping("/franchisee/password/exists/{businessNumber}")
  public ResponseEntity<Boolean> existBusinessNumber(@PathVariable String businessNumber) {
    boolean result = passwordResetService.existBusinessNumber(businessNumber);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/franchisee/password/selfCertification/{businessNumber}")
  public ResponseEntity<Boolean> selfCertification(
      @PathVariable String businessNumber,
      @RequestParam String name,
      @RequestParam String phoneNumber
      ) {
    boolean result = passwordResetService.selfCertification(businessNumber, name, phoneNumber);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/franchisee/password/out/{businessNumber}")
  public ResponseEntity<Boolean> resetOutPassword(
      @PathVariable String businessNumber,
      @RequestBody PasswordChangeRequest passwordChangeRequest
  ) {
    boolean result = passwordResetService.reset(businessNumber, passwordChangeRequest);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/franchisee/password/equals/{franchiseeIndex}")
  public ResponseEntity<Boolean> correctPassword(
      @PathVariable Long franchiseeIndex,
      @RequestParam String password
  ) {
    boolean result = passwordResetService.correctPassword(franchiseeIndex, password);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/franchisee/password/in/{franchiseeIndex}")
  public ResponseEntity<Boolean> resetPassword(
      @PathVariable Long franchiseeIndex,
      @RequestBody PasswordChangeRequest passwordChangeRequest) {
    boolean result = passwordResetService.change(franchiseeIndex, passwordChangeRequest);
    return ResponseEntity.ok(result);
  }
}
