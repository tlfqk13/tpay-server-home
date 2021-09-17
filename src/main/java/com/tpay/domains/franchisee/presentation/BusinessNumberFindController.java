package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.BusinessNumberFindService;
import com.tpay.domains.franchisee.application.dto.BusinessNumberFindRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BusinessNumberFindController {

  private final BusinessNumberFindService businessNumberFindService;

  @PostMapping("/franchisee/business-number")
  public ResponseEntity<String> findBusinessNumber(@RequestBody BusinessNumberFindRequest request) {
    String businessNumber = businessNumberFindService.findBusinessNumber(request);
    return ResponseEntity.ok(businessNumber);
  }
}
