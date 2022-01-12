package com.tpay.domains.franchisee_upload.presentation;


import com.tpay.domains.franchisee_upload.application.FranchiseeBankService;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeBankController {

  private final FranchiseeBankService franchiseeBankService;

  @PostMapping("/franchisee/bank/{franchiseeIndex}")
  public ResponseEntity<FranchiseeBankInfo> save(FranchiseeBankInfo franchiseeBankInfo) {
    FranchiseeBankInfo result = franchiseeBankService.save(franchiseeBankInfo);
    return ResponseEntity.ok(result);
  }
}
