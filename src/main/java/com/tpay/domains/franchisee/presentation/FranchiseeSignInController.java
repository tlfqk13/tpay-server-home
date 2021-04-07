package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeSignInService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignInRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeSignInController {

  private final FranchiseeSignInService franchiseeSignInService;

  @GetMapping("/franchisee")
  public ResponseEntity<FranchiseeSignInResponse> signIn(
      @RequestBody FranchiseeSignInRequest franchiseeSignInRequest) {
    return franchiseeSignInService.signIn(franchiseeSignInRequest);
  }
}
