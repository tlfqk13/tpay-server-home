package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.application.dto.FilterSelector;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfoInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FranchiseeApplicantFindController {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;

    @GetMapping("/admin/franchisee-applicants")
    public ResponseEntity<List<FranchiseeApplicantInfo>> findAll() {
        List<FranchiseeApplicantInfo> responseList = franchiseeApplicantFindService.findAll();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/admin/franchisee-applicants/{filterSelector}/{value}")
    public ResponseEntity<List<FranchiseeApplicantInfo>> applicantFilter(@PathVariable FilterSelector filterSelector, @PathVariable String value) {
        List<FranchiseeApplicantInfo> result = franchiseeApplicantFindService.applicantFilter(filterSelector, value);
        return ResponseEntity.ok(result);
    }
}
