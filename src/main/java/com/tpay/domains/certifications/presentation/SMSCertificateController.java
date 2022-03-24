package com.tpay.domains.certifications.presentation;

import com.tpay.domains.certifications.application.SMSCertificateService;
import com.tpay.domains.certifications.application.dto.CertificateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SMSCertificateController {

    private final SMSCertificateService smsCertificateService;

    @GetMapping("/certifications/{imp_uid}")
    public ResponseEntity<CertificateResponse> certificate(@PathVariable String imp_uid) {
        CertificateResponse response = smsCertificateService.certificate(imp_uid);
        return ResponseEntity.ok(response);
    }
}
