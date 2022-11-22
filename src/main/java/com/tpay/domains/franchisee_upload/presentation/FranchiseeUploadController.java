package com.tpay.domains.franchisee_upload.presentation;


import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 사후면세지정증 이미지 관련
 */
@RestController
@RequestMapping("/franchiseeUpload")
@RequiredArgsConstructor
@Slf4j
public class FranchiseeUploadController {

    private final FranchiseeUploadService franchiseeUploadService;

    /**
     * 사후면세지정증 이미지 업로드
     */
    @PostMapping("/{franchiseeIndex}")
    public ResponseEntity<String> uploadImageAndBankInfo(
            @PathVariable Long franchiseeIndex,
            @RequestParam String imageCategory,
            @RequestParam(required = false, value = "franchiseeBankInfo") String franchiseeBankInfoString,
            @RequestParam MultipartFile uploadImage,
            @KtpIndexInfo IndexInfo indexInfo) {
        String s3Path = franchiseeUploadService.uploadImageAndBankInfo(indexInfo.getIndex(), franchiseeBankInfoString, imageCategory, uploadImage);
        return ResponseEntity.ok(s3Path);
    }

    /**
     * 사후면세지정증 이미지 수정
     */
    @PatchMapping("/{franchiseeIndex}")
    public ResponseEntity<String> uploadUpdateImageAndBankInfo(
        @PathVariable Long franchiseeIndex,
        @RequestParam(required = false) String imageCategory,
        @RequestParam("franchiseeBankInfo") String franchiseeBankInfoString,
        @RequestParam(required = false) MultipartFile uploadImage,
        @KtpIndexInfo IndexInfo indexInfo) {

        String s3Path = franchiseeUploadService.uploadUpdateImageAndBankInfo(indexInfo.getIndex(), franchiseeBankInfoString, imageCategory, uploadImage);
        return ResponseEntity.ok(s3Path);

    }
}
