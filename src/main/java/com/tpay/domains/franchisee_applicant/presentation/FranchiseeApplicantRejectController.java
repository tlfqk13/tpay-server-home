package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantRejectRequest;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantRejectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantRejectController {

  private final FranchiseeApplicantRejectService franchiseeApplicantRejectService;

  @PatchMapping("/admin/franchisee-applicant/{franchiseeApplicantIndex}")
  public ResponseEntity reject(
      @PathVariable Long franchiseeApplicantIndex,
      @RequestBody FranchiseeApplicantRejectRequest rejectRequest) {
    franchiseeApplicantRejectService.reject(
        franchiseeApplicantIndex, rejectRequest.getRejectReason());
    return ResponseEntity.ok().build();
  }
}
