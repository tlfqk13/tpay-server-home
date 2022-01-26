package com.tpay.domains.franchisee_upload.presentation;


import com.tpay.domains.franchisee_upload.application.FranchiseeUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FranchiseeUploadController {

  private final FranchiseeUploadService franchiseeUploadService;

  @PostMapping("/franchiseeUpload/{franchiseeIndex}")
  public ResponseEntity<String> uploadImageAndBankInfo(
      @PathVariable Long franchiseeIndex,
      @RequestParam String imageCategory,
      @RequestParam("franchiseeBankInfo") String franchiseeBankInfoString,
      @RequestParam MultipartFile uploadImage) {

    String s3Path = franchiseeUploadService.uploadImageAndBankInfo(franchiseeIndex, franchiseeBankInfoString, imageCategory, uploadImage);
    return ResponseEntity.ok(s3Path);
  }

  @PatchMapping("/franchiseeUpload/{franchiseeIndex}")
  public ResponseEntity<String> uploadUpdateImageAndBankInfo(
      @PathVariable Long franchiseeIndex,
      @RequestParam(required = false) String imageCategory,
      @RequestParam("franchiseeBankInfo") String franchiseeBankInfoString,
      @RequestParam(required = false) MultipartFile uploadImage) {

    String s3Path = franchiseeUploadService.uploadUpdateImageAndBankInfo(franchiseeIndex, franchiseeBankInfoString, imageCategory, uploadImage);
    return ResponseEntity.ok(s3Path);

  }
}
