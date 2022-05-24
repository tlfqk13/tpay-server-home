package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.*;
import com.tpay.domains.franchisee_applicant.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 가맹점 신청 관련 Admin 페이지 기능
 * 가맹점 수락/거절
 * 가맹점 신청 기본/상세 조회
 * 사후면세판매장 지정증 업데이트
 * 읽음처리
 */
@RestController
@RequestMapping("/admin/franchisee-applicants")
@RequiredArgsConstructor
public class AdminFranchiseeApplicant {

    private final FranchiseeApplicantAcceptService franchiseeApplicantAcceptService;
    private final FranchiseeApplicantDetailService franchiseeApplicantDetailService;
    private final FranchiseeApplicantSetNumberService franchiseeApplicantSetNumberService;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final FranchiseeApplicantReadService franchiseeApplicantReadService;
    private final FranchiseeApplicantRejectService franchiseeApplicantRejectService;

    /**
     * 가맹점 신청 수락
     */
    @PutMapping("/{franchiseeApplicantIndex}")
    public ResponseEntity<FranchiseeFindRequest> accept(
        @PathVariable Long franchiseeApplicantIndex, @RequestBody FranchiseeFindRequest franchiseeFindRequest) {
        FranchiseeFindRequest response =
            franchiseeApplicantAcceptService.accept(franchiseeApplicantIndex, franchiseeFindRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 가맹점 신청 거절
     */
    @PatchMapping("/{franchiseeApplicantIndex}")
    public ResponseEntity reject(
        @PathVariable Long franchiseeApplicantIndex,
        @RequestBody FranchiseeApplicantRejectRequest rejectRequest) {
        franchiseeApplicantRejectService.reject(
            franchiseeApplicantIndex, rejectRequest.getRejectReason());
        return ResponseEntity.ok().build();
    }

    /**
     * 가맹점 신청 전체 내역 조회
     */
    @GetMapping
    public ResponseEntity<List<FranchiseeApplicantInfo>> findAll() {
        List<FranchiseeApplicantInfo> responseList = franchiseeApplicantFindService.findAll();
        return ResponseEntity.ok(responseList);
    }


    /**
     * 가맹점 신청 상세 정보
     */
    @GetMapping("/{franchiseeApplicantIndex}")
    public ResponseEntity<FranchiseeApplicantDetailResponse> findOne(@PathVariable Long franchiseeApplicantIndex) {
        FranchiseeApplicantDetailResponse result = franchiseeApplicantDetailService.detail(franchiseeApplicantIndex);
        return ResponseEntity.ok(result);
    }

    /**
     * 가맹점 신청 내역 필터링
     */
    @GetMapping("/{filterSelector}/{value}")
    public ResponseEntity<List<FranchiseeApplicantInfo>> filter(@PathVariable FilterSelector filterSelector, @PathVariable String value) {
        List<FranchiseeApplicantInfo> result = franchiseeApplicantFindService.applicantFilter(filterSelector, value);
        return ResponseEntity.ok(result);
    }

    /**
     * 지정증 업데이트
     */
    @PatchMapping("/{franchiseeApplicantIndex}/taxFreeStoreNumber")
    public ResponseEntity<String> updateTaxFreeStoreNumber(@PathVariable Long franchiseeApplicantIndex, @RequestBody FranchiseeApplicantSetNumberRequest franchiseeApplicantSetNumberRequest) {
        String result = franchiseeApplicantSetNumberService.updateTaxFreeStoreNumber(franchiseeApplicantIndex, franchiseeApplicantSetNumberRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 읽음처리 (New 표기 제거)
     */
    @PatchMapping("/check/{franchiseeApplicantIndex}")
    public ResponseEntity<Boolean> read(@PathVariable Long franchiseeApplicantIndex) {
        boolean result = franchiseeApplicantReadService.read(franchiseeApplicantIndex);
        return ResponseEntity.ok(result);
    }



}
