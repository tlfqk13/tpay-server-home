package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeSignUpService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeSignUpController {

  private final FranchiseeSignUpService franchiseeSignUpService;

  @PostMapping("/sign-up")
  public ResponseEntity<FranchiseeSignUpResponse> signUp(
      @RequestBody FranchiseeSignUpRequest request) {
    FranchiseeSignUpResponse response = franchiseeSignUpService.signUp(request);
    return ResponseEntity.ok(response);
  }
}
