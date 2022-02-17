package com.tpay.domains.franchisee.application.dto.cms;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeCmsResponse {
  private String totalCount;
  private String totalAmount;
  private String totalVat;
  private String totalCommission;
}
