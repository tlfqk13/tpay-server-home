package com.tpay.domains.certifications.presentation;

import com.tpay.domains.certifications.application.SMSCertificateService;
import com.tpay.domains.certifications.application.dto.CertificateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SMSCertificateController {

  private final SMSCertificateService smsCertificateService;

  @GetMapping("/certifications")
  public ResponseEntity<CertificateResponse> certificate(
      @RequestParam(value = "imp_uid") String imp_uid) {
    return smsCertificateService.certificate(imp_uid);
  }
}
