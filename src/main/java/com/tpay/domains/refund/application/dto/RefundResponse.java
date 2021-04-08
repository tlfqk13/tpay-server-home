package com.tpay.domains.refund.application.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class RefundResponse {
  private String responseCode;
  private String message;
  private String purchaseSequenceNumber;
  private String takeoutNumber;
  private String beforeDeduction;
  private String afterDeduction;
}
