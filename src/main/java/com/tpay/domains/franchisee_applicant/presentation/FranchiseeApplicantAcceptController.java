package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantAcceptService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeInfo;
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
  public ResponseEntity<FranchiseeInfo> accept(
      @PathVariable Long franchiseeApplicantIndex, @RequestBody FranchiseeInfo franchiseeInfo) {
    FranchiseeInfo response =
        franchiseeApplicantAcceptService.accept(franchiseeApplicantIndex, franchiseeInfo);
    return ResponseEntity.ok(response);
  }
}
