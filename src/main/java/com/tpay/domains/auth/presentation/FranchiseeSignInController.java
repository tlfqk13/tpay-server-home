package com.tpay.domains.auth.presentation;

import com.tpay.domains.auth.application.FranchiseeSignInService;
import com.tpay.domains.auth.application.dto.FranchiseeSignInRequest;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeSignInController {

  private final FranchiseeSignInService franchiseeSignInService;

  @PostMapping("/sign-in")
  public ResponseEntity<FranchiseeTokenInfo> signIn(@RequestBody FranchiseeSignInRequest request) {
    FranchiseeTokenInfo response = franchiseeSignInService.signIn(request);
    return ResponseEntity.ok(response);
  }
}
