package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfoInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantFindController {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;

  @GetMapping("/admin/franchisee-applicants")
  public ResponseEntity<List<FranchiseeApplicantInfoInterface>> findAll() {
    List<FranchiseeApplicantInfoInterface> responseList = franchiseeApplicantFindService.findAll();
    return ResponseEntity.ok(responseList);
  }

}
