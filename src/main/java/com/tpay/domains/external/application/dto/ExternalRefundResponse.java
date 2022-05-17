package com.tpay.domains.external.application.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExternalRefundResponse {
    private String responseCode;
    private Integer payment;
    private String message;
}
