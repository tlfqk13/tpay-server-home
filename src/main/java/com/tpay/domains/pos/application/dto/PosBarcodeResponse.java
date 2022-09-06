package com.tpay.domains.pos.application.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PosBarcodeResponse {

    private Long externalRefundIndex;
    private String s3Path;
}
