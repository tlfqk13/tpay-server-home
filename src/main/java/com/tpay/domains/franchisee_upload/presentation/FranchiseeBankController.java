package com.tpay.domains.franchisee_upload.presentation;


import com.tpay.domains.franchisee_upload.application.FranchiseeBankService;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FranchiseeBankController {

  private final FranchiseeBankService franchiseeBankService;

  @PostMapping("/franchisee/bank/{franchiseeIndex}")
  public ResponseEntity<FranchiseeBankInfo> save(
      @PathVariable Long franchiseeIndex,
      @RequestBody FranchiseeBankInfo franchiseeBankInfo
  ) {
    FranchiseeBankInfo result = franchiseeBankService.save(franchiseeIndex,franchiseeBankInfo);
    return ResponseEntity.ok(result);
  }
}
