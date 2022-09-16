package com.tpay.domains.franchisee_applicant.presentation;

import com.tpay.domains.franchisee_applicant.application.*;
import com.tpay.domains.franchisee_applicant.application.dto.*;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 가맹점 신청 관련 Admin 페이지 기능
 * 가맹점 수락/거절
 * 가맹점 신청 기본/상세 조회
 * 사후면세판매장 지정증 업데이트
 * 읽음처리
 */
@RestController
@Api(tags = "어드민 - 가맹점 신청 관련")
@RequestMapping("/admin/franchisee-applicants")
@RequiredArgsConstructor
public class AdminFranchiseeApplicant {

    private final FranchiseeApplicantAcceptService franchiseeApplicantAcceptService;
    private final FranchiseeApplicantDetailService franchiseeApplicantDetailService;
    private final FranchiseeApplicantSetNumberService franchiseeApplicantSetNumberService;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final FranchiseeApplicantReadService franchiseeApplicantReadService;
    private final FranchiseeApplicantRejectService franchiseeApplicantRejectService;
    private final FranchiseeApplicantSetBalancePerService franchiseeApplicantSetBalancePerService;
    private final FranchiseeUploadService franchiseeUploadService;

    /**
     * 가맹점 신청 수락
     */
    @PutMapping("/{franchiseeApplicantIndex}")
    @ApiOperation(value = "가맹점 신청 수락", notes = "어드민에서 가맹점 신청 수락시 로직")
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
    @ApiOperation(value = "가맹점 신청 거절", notes = "어드민에서 가맹점 신청 거절시 로직")
    public ResponseEntity reject(
        @PathVariable Long franchiseeApplicantIndex,
        @RequestBody FranchiseeApplicantRejectRequest rejectRequest) {
        franchiseeApplicantRejectService.reject(
            franchiseeApplicantIndex, rejectRequest.getRejectReason());
        return ResponseEntity.ok().build();
    }


    @GetMapping("")
    public ResponseEntity<FranchiseeApplicantFindResponse> findAll(
            @RequestParam int page,
            @RequestParam String searchKeyword
    ){
        FranchiseeApplicantFindResponse responseList = franchiseeApplicantFindService.findAll(page,searchKeyword);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 가맹점 신청 상세 정보
     */
    @GetMapping("/{franchiseeApplicantIndex}")
    @ApiOperation(value = "가맹점 신청 내역조회", notes = "가맹점 현황 내 상세보기")
    public ResponseEntity<FranchiseeApplicantDetailResponse> findOne(@PathVariable Long franchiseeApplicantIndex) {
        FranchiseeApplicantDetailResponse result = franchiseeApplicantDetailService.detail(franchiseeApplicantIndex);
        return ResponseEntity.ok(result);
    }

    /**
     * 가맹점 신청 내역 필터링
     */
    @GetMapping("/{filterSelector}/{value}")
    @ApiOperation(value = "가맹점 신청 내역조회", notes = "가맹점 현황 필터링")
    public ResponseEntity<FranchiseeApplicantFindResponse> filter(
            @PathVariable FilterSelector filterSelector,
            @PathVariable String value,
            @RequestParam int page,
            @RequestParam String searchKeyword
    ) {
        FranchiseeApplicantFindResponse result = franchiseeApplicantFindService.applicantFilter(filterSelector, value, page, searchKeyword);
        return ResponseEntity.ok(result);
    }

    /**
     * 지정증 업데이트
     */
    @PatchMapping("/{franchiseeApplicantIndex}/taxFreeStoreNumber")
    @ApiOperation(value = "가맹점 신청 내역조회", notes = "가맹점 현황 내 상세보기 내 사후면세지정증 수기 업데이트")
    public ResponseEntity<String> updateTaxFreeStoreNumber(@PathVariable Long franchiseeApplicantIndex, @RequestBody FranchiseeApplicantSetNumberRequest franchiseeApplicantSetNumberRequest) {
        String result = franchiseeApplicantSetNumberService.updateTaxFreeStoreNumber(franchiseeApplicantIndex, franchiseeApplicantSetNumberRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 읽음처리 (New 표기 제거)
     */
    @PatchMapping("/check/{franchiseeApplicantIndex}")
    @ApiOperation(value = "가맹점 신청 내역조회", notes = "가맹점 현황 읽음처리")
    public ResponseEntity<Boolean> read(@PathVariable Long franchiseeApplicantIndex) {
        boolean result = franchiseeApplicantReadService.read(franchiseeApplicantIndex);
        return ResponseEntity.ok(result);
    }

    /**
     * 포인트 적립 비율 업데이트
     */
    @PatchMapping("/{franchiseeApplicantIndex}/balancePercentage")
    @ApiOperation(value = "가맹점 포인트 적립 비율 업데이트  ", notes = "가맹점 포인트 적립 비율 업데이트 ")
    public ResponseEntity<String> updateBalancePercentage(
            @PathVariable Long franchiseeApplicantIndex
            ,@RequestBody FranchiseeApplicantSetBalancePerRequest franchiseeApplicantSetBalancePerRequest) {
        String result = String.valueOf(franchiseeApplicantSetBalancePerService.updateBalancePercentage(franchiseeApplicantIndex, franchiseeApplicantSetBalancePerRequest));
        return ResponseEntity.ok(result);
    }

    /**
     * 어드민에서 지정증 및 은행 정보 기입
     */
    @PostMapping("/franchiseeUpload/{franchiseeIndex}")
    public ResponseEntity<String> uploadImageAndBankInfo(
            @PathVariable Long franchiseeIndex,
            @RequestParam String imageCategory,
            @RequestParam("franchiseeBankInfo") String franchiseeBankInfoString,
            @RequestParam MultipartFile uploadImage) {
        String s3Path = franchiseeUploadService.uploadImageAndBankInfo(franchiseeIndex, franchiseeBankInfoString, imageCategory, uploadImage);
        return ResponseEntity.ok(s3Path);
    }
}
