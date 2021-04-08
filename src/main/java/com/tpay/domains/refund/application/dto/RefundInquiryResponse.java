package com.tpay.domains.refund.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class RefundInquiryResponse {
  private Long userIndex;
  private RefundResponse refundResponse;
}
