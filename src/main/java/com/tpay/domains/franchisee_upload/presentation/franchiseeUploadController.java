package com.tpay.domains.franchisee_upload.presentation;


import com.tpay.domains.franchisee_upload.application.FranchiseeUploadService;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class franchiseeUploadController {

  private final FranchiseeUploadService franchiseeUploadService;

  @PostMapping("/uploadTest/{franchiseeIndex}")
  public ResponseEntity uploadDocuments(
      @PathVariable Long franchiseeIndex,
//      @RequestParam FranchiseeUploadRequest franchiseeUploadRequest,
      @RequestParam MultipartFile businessLicenseImage) {
    franchiseeUploadService.uploadDocuments(franchiseeIndex,businessLicenseImage) ;
    return ResponseEntity.ok().build();
  }
}
