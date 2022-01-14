package com.tpay.domains.certifications.presentation;


import com.tpay.domains.certifications.application.BusinessLicenseValidationService;
import com.tpay.domains.certifications.application.dto.BusinessValidRequest;
import com.tpay.domains.certifications.application.dto.BusinessValidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BusinessLicenseValidationController {

  private final BusinessLicenseValidationService businessLicenseValidationService;

  @PostMapping("validate/business")
  public ResponseEntity<BusinessValidResponse> validCheckBusinessLicense(@RequestBody BusinessValidRequest businessValidRequest) {
    BusinessValidResponse result = businessLicenseValidationService.valid(businessValidRequest);
    return ResponseEntity.ok(result);
  }
}
