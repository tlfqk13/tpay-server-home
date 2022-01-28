package com.tpay.domains.franchisee.application.dto.vat;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VatDetailResponseDetailInfo {
  private String purchaseSerialNumber;
  private String saleDate;
  private String takeoutConfirmNumber;
  private String amount;
  private String vat;
}
