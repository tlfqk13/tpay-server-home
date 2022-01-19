//package com.tpay.domains.franchisee_applicant.presentation;
//
//import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantDetailService;
//import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDetailResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class FranchiseeApplicantDetailController {
//
//  private final FranchiseeApplicantDetailService franchiseeApplicantDetailService;
//
//  @GetMapping("/admin/franchisee-applicants/{franchiseeApplicantIndex}")
//  public ResponseEntity<FranchiseeApplicantDetailResponse> detail(@PathVariable Long franchiseeApplicantIndex){
//    FranchiseeApplicantDetailResponse result = franchiseeApplicantDetailService.detail(franchiseeApplicantIndex);
//    return ResponseEntity.ok(result);
//  }
//}
