package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantStatusInfo;
import com.tpay.domains.franchisee_applicant.application.FranchiseeStatusFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeStatusFindController {

  private final FranchiseeStatusFindService franchiseeStatusFindService;

  @GetMapping("/franchisee-applicant/{franchiseeApplicantIndex}")
  public ResponseEntity<FranchiseeApplicantStatusInfo> findByIndex(
      @PathVariable Long franchiseeApplicantIndex) {
    FranchiseeApplicantStatusInfo response = franchiseeStatusFindService.findByIndex(franchiseeApplicantIndex);
    return ResponseEntity.ok(response);
  }
}
