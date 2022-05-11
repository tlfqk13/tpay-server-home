package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeStatusFindService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantStatusInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeStatusFindController {

    private final FranchiseeStatusFindService franchiseeStatusFindService;

    //주의 franchisee-applicant 임 's' 없음
    // TODO: 2022/05/11 jwt 신규검증 제외됨
    @GetMapping("/franchisee-applicant/{franchiseeApplicantIndex}")
    public ResponseEntity<FranchiseeApplicantStatusInfo> findByIndex(
        @PathVariable Long franchiseeApplicantIndex) {
        FranchiseeApplicantStatusInfo response = franchiseeStatusFindService.findByIndex(franchiseeApplicantIndex);
        return ResponseEntity.ok(response);
    }
}
