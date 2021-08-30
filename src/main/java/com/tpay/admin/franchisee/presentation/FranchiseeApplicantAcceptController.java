package com.tpay.admin.franchisee.presentation;

import com.tpay.admin.franchisee.application.dto.FranchiseeApplicantRejectRequest;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantAcceptController {

  private final FranchiseeApplicantUpdateService franchiseeApplicantUpdateService;

  @PatchMapping("/admin/franchisee-applicant/{franchiseeApplicantIndex}")
  public ResponseEntity reject(
      @PathVariable Long franchiseeApplicantIndex,
      @RequestBody FranchiseeApplicantRejectRequest rejectRequest) {
    franchiseeApplicantUpdateService.reject(
        franchiseeApplicantIndex, rejectRequest.getRejectReason());
    return ResponseEntity.ok().build();
  }
}
