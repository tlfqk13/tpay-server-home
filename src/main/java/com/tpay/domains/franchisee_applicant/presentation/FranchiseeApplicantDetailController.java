package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantDetailService;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantSetNumberService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDetailResponse;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantSetNumberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantDetailController {

  private final FranchiseeApplicantDetailService franchiseeApplicantDetailService;
  private final FranchiseeApplicantSetNumberService franchiseeApplicantSetNumberService;

  @GetMapping("/admin/franchisee-applicants/{franchiseeApplicantIndex}")
  public ResponseEntity<FranchiseeApplicantDetailResponse> detail(@PathVariable Long franchiseeApplicantIndex){
    FranchiseeApplicantDetailResponse result = franchiseeApplicantDetailService.detail(franchiseeApplicantIndex);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/admin/franchisee-applicants/{franchiseeApplicantIndex}/taxFreeStoreNumber")
  public ResponseEntity<String> updateTaxFreeStoreNumber(@PathVariable Long franchiseeApplicantIndex, @RequestBody FranchiseeApplicantSetNumberRequest franchiseeApplicantSetNumberRequest){
    String result = franchiseeApplicantSetNumberService.updateTaxFreeStoreNumber(franchiseeApplicantIndex, franchiseeApplicantSetNumberRequest);
    return ResponseEntity.ok(result);
  }
}
