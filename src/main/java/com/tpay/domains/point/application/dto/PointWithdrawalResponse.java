package com.tpay.domains.point.application.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointWithdrawalResponse {
  private Long amount;
  private String bankName;
  private String accountNumber;
  private Long restPoint;
}
