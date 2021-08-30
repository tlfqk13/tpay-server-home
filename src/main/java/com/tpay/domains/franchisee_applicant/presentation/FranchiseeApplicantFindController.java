package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantFindResponse;
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

  @GetMapping("/franchisee-applicants")
  public ResponseEntity<List<FranchiseeApplicantFindResponse>> findAll() {
    List<FranchiseeApplicantFindResponse> responseList = franchiseeApplicantFindService.findAll();
    return ResponseEntity.ok(responseList);
  }

  @GetMapping("/franchisee-applicants/{franchiseeApplicantIndex}")
  public ResponseEntity<FranchiseeApplicantFindResponse> findByIndex(
      @PathVariable Long franchiseeApplicantIndex) {
    FranchiseeApplicantFindResponse response =
        franchiseeApplicantFindService.find(franchiseeApplicantIndex);
    return ResponseEntity.ok(response);
  }
}
