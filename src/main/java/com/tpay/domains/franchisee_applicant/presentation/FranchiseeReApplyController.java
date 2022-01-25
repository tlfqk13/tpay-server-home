package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeReApplyService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantReapplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeReApplyController {

  private final FranchiseeReApplyService franchiseeReApplyService;



  @GetMapping("/franchisee-applicants/reapply/{franchiseeApplicantIndex}")
  public ResponseEntity<FranchiseeApplicantReapplyResponse> findBaseInfo(@PathVariable Long franchiseeApplicantIndex) {
    FranchiseeApplicantReapplyResponse result = franchiseeReApplyService.findBaseInfo(franchiseeApplicantIndex);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/franchisee-applicants/{businessNumber}")
  public ResponseEntity<FranchiseeApplicantInfo> reapply(@PathVariable String businessNumber) {
    FranchiseeApplicantInfo response = franchiseeReApplyService.reapply(businessNumber);
    return ResponseEntity.ok(response);
  }
}
