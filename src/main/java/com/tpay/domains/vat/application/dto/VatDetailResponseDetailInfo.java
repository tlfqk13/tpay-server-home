package com.tpay.domains.vat.application.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VatDetailResponseDetailInfo {
    private String purchaseSerialNumber;
    private String saleDate;
    private String takeoutConfirmNumber;
    private String refundAmount;
    private String amount;
    private String vat;
}
