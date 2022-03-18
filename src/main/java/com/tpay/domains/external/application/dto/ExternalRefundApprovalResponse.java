package com.tpay.domains.external.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExternalRefundApprovalResponse {
  private String message;
}
