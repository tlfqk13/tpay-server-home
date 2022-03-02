package com.tpay.domains.auth.presentation;


import com.tpay.domains.auth.application.SignInService;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
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
  public ResponseEntity<SignInTokenInfo> signIn(@RequestBody SignInRequest signInRequest) {
    SignInTokenInfo signInTokenInfo = signInService.signIn(signInRequest);
    return ResponseEntity.ok(signInTokenInfo);
  }

}
