package com.tpay.domains.vat.application.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VatDetailResponseTotalInfo {
    private String totalCount;
    private String totalAmount;
    private String totalVat;

}
