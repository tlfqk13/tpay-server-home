package com.tpay.domains.certifications.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class TokenRequest {
    private String imp_key;
    private String imp_secret;
}
