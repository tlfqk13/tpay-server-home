package com.tpay.domains.certifications.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CertificateResponse {
    private Long code;
    private String name;
    private String phoneNumber;
}
