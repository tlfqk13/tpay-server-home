package com.tpay.domains.refund.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class RefundInquiryResponse {
  private String responseCode;
  private String beforeDeduction;
  private String afterDeduction;
  private String errorCode;
  private String message;
}
