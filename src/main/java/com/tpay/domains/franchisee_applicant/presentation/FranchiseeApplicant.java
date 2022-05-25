package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.FranchiseeReApplyService;
import com.tpay.domains.franchisee_applicant.application.FranchiseeStatusFindService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantReapplyResponse;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantStatusInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 앱 내의 가맹점 신청 관련 기능
 * 1. 가맹점 상태조회 (franchiseeStatus)
 * 2. 가맹점 재신청시 기본 정보 조회
 * 3. 가맹점 재신청
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/franchisee-applicants")
public class FranchiseeApplicant {

    private final FranchiseeStatusFindService franchiseeStatusFindService;
    private final FranchiseeReApplyService franchiseeReApplyService;

    /**
     * 가맹점 신청 상태조회
     */
    @GetMapping("/{franchiseeApplicantIndex}")
    public ResponseEntity<FranchiseeApplicantStatusInfo> findByIndex(
        @PathVariable Long franchiseeApplicantIndex) {
        FranchiseeApplicantStatusInfo response = franchiseeStatusFindService.findByIndex(franchiseeApplicantIndex);
        return ResponseEntity.ok(response);
    }

    /**
     * 가맹점 재신청시 기존 정보 조회
     */
    @GetMapping("/reapply/{franchiseeIndex}")
    public ResponseEntity<FranchiseeApplicantReapplyResponse> findBaseInfo(@PathVariable Long franchiseeIndex) {
        FranchiseeApplicantReapplyResponse result = franchiseeReApplyService.findBaseInfo(franchiseeIndex);
        return ResponseEntity.ok(result);
    }

    /**
     * 가맹점 재신청 요청
     */
    @PostMapping("/{businessNumber}")
    public ResponseEntity<FranchiseeApplicantInfo> reapply(@PathVariable String businessNumber) {
        FranchiseeApplicantInfo response = franchiseeReApplyService.reapply(businessNumber);
        return ResponseEntity.ok(response);
    }
}
