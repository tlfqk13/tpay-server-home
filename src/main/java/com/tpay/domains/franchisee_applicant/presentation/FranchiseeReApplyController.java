package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeReApplyService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeReApplyController {

  private final FranchiseeReApplyService franchiseeReApplyService;

  @PostMapping("/admin/franchisee-applicants/{businessNumber}")
  public ResponseEntity<FranchiseeApplicantInfo> reapply(@PathVariable String businessNumber) {
    FranchiseeApplicantInfo response = franchiseeReApplyService.reapply(businessNumber);
    return ResponseEntity.ok(response);
  }
}
