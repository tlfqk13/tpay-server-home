package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.application.dto.FranchiseeMyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeFindController {

  private final FranchiseeFindService franchiseeFindService;

  @GetMapping("/franchisee/{franchiseeIndex}")
  public ResponseEntity<FranchiseeMyPageResponse> findMyPageInfo(@PathVariable Long franchiseeIndex) {
    FranchiseeMyPageResponse response = franchiseeFindService.findMyPageInfo(franchiseeIndex);
    return ResponseEntity.ok(response);
  }
}
