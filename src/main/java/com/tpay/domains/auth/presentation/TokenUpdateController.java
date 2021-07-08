package com.tpay.domains.auth.presentation;

import com.tpay.domains.auth.application.TokenUpdateService;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
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
  public ResponseEntity<FranchiseeTokenInfo> refresh(
      @RequestBody FranchiseeTokenInfo franchiseeTokenInfo) {
    return ResponseEntity.ok(tokenUpdateService.refresh(franchiseeTokenInfo));
  }
}
