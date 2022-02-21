package com.tpay.domains.auth.presentation;


import com.tpay.domains.auth.application.SignInService;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignInController {

  private final SignInService signInService;

  @PostMapping("/sign-in")
  public ResponseEntity<FranchiseeTokenInfo> signIn(@RequestBody SignInRequest signInRequest) {
    FranchiseeTokenInfo franchiseeTokenInfo = signInService.signIn(signInRequest);
    return ResponseEntity.ok(franchiseeTokenInfo);
  }

}
