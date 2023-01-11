package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.application.FranchiseeReApplyService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantReapplyResponse;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantStatusInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
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

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final PushHistoryRepository pushHistoryRepository;
    private final FranchiseeReApplyService franchiseeReApplyService;

    /**
     * 가맹점 신청 상태조회
     */
    @GetMapping("/{franchiseeApplicantIndex}")
    public ResponseEntity<FranchiseeApplicantStatusInfo> findByIndex(
            @PathVariable Long franchiseeApplicantIndex) {
        FranchiseeApplicantEntity franchiseeApplicant
                = franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);
        long count = pushHistoryRepository.countByUserIdAndIsRead(franchiseeApplicant.getFranchiseeEntity().getId(), false);

        FranchiseeApplicantStatusInfo response = FranchiseeApplicantStatusInfo.builder()
                .franchiseeStatus(franchiseeApplicant.getFranchiseeStatus())
                .count(count)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 가맹점 재신청시 기존 정보 조회
     */
    @GetMapping
    public ResponseEntity<FranchiseeApplicantReapplyResponse> findBaseInfo(
            @KtpIndexInfo IndexInfo indexInfo) {
        FranchiseeApplicantReapplyResponse result = franchiseeReApplyService.findBaseInfo(indexInfo.getIndex());
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
