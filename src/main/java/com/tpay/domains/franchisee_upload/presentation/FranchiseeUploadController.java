package com.tpay.domains.franchisee_upload.presentation;


import com.tpay.domains.franchisee_upload.application.FranchiseeUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FranchiseeUploadController {

  private final FranchiseeUploadService franchiseeUploadService;

  @PostMapping("/franchiseeUpload/{franchiseeIndex}")
  public ResponseEntity<String> uploadDocuments(
      @PathVariable Long franchiseeIndex,
      @RequestParam String imageCategory,
      @RequestParam("FranchiseeBankInfo") String franchiseeBankInfoString,
//      @RequestParam FranchiseeUploadRequest franchiseeUploadRequest,
      @RequestParam MultipartFile uploadImage) {

    String s3Path = franchiseeUploadService.uploadDocuments(franchiseeIndex, franchiseeBankInfoString, imageCategory, uploadImage);
    return ResponseEntity.ok(s3Path);
  }
}
