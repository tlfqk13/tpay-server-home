package com.tpay.domains.auth.presentation;


import com.tpay.domains.auth.application.SignOutService;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignOutController {

  private final SignOutService signOutService;

  @DeleteMapping("/sign-out")
  public ResponseEntity<String> signOut(@RequestBody SignOutRequest signOutRequest){
    String result = signOutService.signOut(signOutRequest);
    return ResponseEntity.ok(result);
  }
}
