package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantFindController {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;

  @GetMapping("/admin/franchisee-applicants")
  public ResponseEntity<List<FranchiseeApplicantInfo>> findAll() {
    List<FranchiseeApplicantInfo> responseList = franchiseeApplicantFindService.findAll();
    return ResponseEntity.ok(responseList);
  }

  @GetMapping("/admin/franchisee-applicants/{franchiseeApplicantIndex}")
  public ResponseEntity<FranchiseeApplicantInfo> findByIndex(
      @PathVariable Long franchiseeApplicantIndex) {
    FranchiseeApplicantInfo response =
        franchiseeApplicantFindService.find(franchiseeApplicantIndex);
    return ResponseEntity.ok(response);
  }
}
