package com.tpay.domains.point.application.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointWithdrawalResponse {
  private String amount;
  private String bankName;
  private String accountNumber;
  private String restPoint;
}
