package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantAcceptService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeFindRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantAcceptController {

  private final FranchiseeApplicantAcceptService franchiseeApplicantAcceptService;

  @PutMapping("/admin/franchisee-applicants/{franchiseeApplicantIndex}")
  public ResponseEntity<FranchiseeFindRequest> accept(
      @PathVariable Long franchiseeApplicantIndex, @RequestBody FranchiseeFindRequest franchiseeFindRequest) {
    FranchiseeFindRequest response =
        franchiseeApplicantAcceptService.accept(franchiseeApplicantIndex, franchiseeFindRequest);
    return ResponseEntity.ok(response);
  }
}
