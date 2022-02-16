package com.tpay.domains.franchisee.application.dto.cms;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeCmsDetailResponse {
  private List<String> commissionInfoList;
  private List<String> customerInfoList;
}
