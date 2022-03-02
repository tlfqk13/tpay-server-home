package com.tpay.domains.auth.presentation;

import com.tpay.domains.auth.application.TokenUpdateService;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenUpdateController {

  private final TokenUpdateService tokenUpdateService;

  @PatchMapping("/refresh")
  public ResponseEntity<SignInTokenInfo> refresh(
      @RequestBody SignInTokenInfo signInTokenInfo) {
    return ResponseEntity.ok(tokenUpdateService.refresh(signInTokenInfo));
  }
}
