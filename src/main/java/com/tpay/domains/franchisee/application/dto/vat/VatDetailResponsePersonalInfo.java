package com.tpay.domains.franchisee.application.dto.vat;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VatDetailResponsePersonalInfo {
  private String sellerName;
  private String businessNumber;
  private String storeName;
  private String storeAddress;
  private String saleTerm;
  private String taxFreeStoreNumber;
}
