package com.tpay.domains.auth.presentation;

import com.tpay.domains.auth.application.FranchiseeSignUpService;
import com.tpay.domains.auth.application.dto.FranchiseeSignUpRequest;
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
  public ResponseEntity signUp(@RequestBody FranchiseeSignUpRequest request) {
    franchiseeSignUpService.signUp(request);
    return ResponseEntity.ok().build();
  }
}
