package com.tpay.domains.franchisee_applicant.presentation;


import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantReadController {

  private final FranchiseeApplicantReadService franchiseeApplicantReadService;

  @PatchMapping("/admin/franchisee-applicants/check/{franchiseeApplicantIndex}")
  public ResponseEntity read(@PathVariable Long franchiseeApplicantIndex){
    franchiseeApplicantReadService.read(franchiseeApplicantIndex);
    return ResponseEntity.ok().build();
  }
}
