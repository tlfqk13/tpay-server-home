package com.tpay.domains.auth.presentation;

import com.tpay.domains.auth.application.FranchiseeSignOutService;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeSignOutController {

  private final FranchiseeSignOutService franchiseeSignOutService;

  @DeleteMapping("/sign-out")
  public ResponseEntity signOut(@RequestBody FranchiseeTokenInfo franchiseeTokenInfo)
      throws IOException {
    franchiseeSignOutService.signOut(franchiseeTokenInfo);
    return ResponseEntity.ok().build();
  }
}
