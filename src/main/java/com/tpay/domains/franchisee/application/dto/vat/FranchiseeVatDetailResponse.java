package com.tpay.domains.franchisee.application.dto.vat;


import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeVatDetailResponse {
  private VatDetailResponsePersonalInfo vatDetailResponsePersonalInfo;
  private VatDetailResponseTotalInfo vatDetailResponseTotalInfo;
  private List<VatDetailResponseDetailInfo> vatDetailResponseDetailInfoList;
}
