package com.tpay.domains.certifications.presentation;

import com.tpay.domains.certifications.application.SMSCertificateService;
import com.tpay.domains.certifications.application.dto.CertificateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Iamport 관련
 *
 * 본인인증시 사용되는 클래스로 프론트에서 인증하기 버튼을 눌러 자기 정보를 입력하여 문제가 없을 경우 uid를 하나 내려준다.
 * uid를 기반으로 프론트엔드는 백엔드(서버)에게 Get요청을 보낸다. 그 핸들러(컨트롤러)가 이 클래스이다.
 * uid를 기반으로 서버는 iamport 서버에게 개인정보를 요청하여 우리에게 필요한 적절한 응답값만 build하여 리턴한다.
 */
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
