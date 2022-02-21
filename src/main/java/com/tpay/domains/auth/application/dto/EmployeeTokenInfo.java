package com.tpay.domains.auth.application.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeTokenInfo {
  private Long employeeIndex;
  private String userId;
  private String name;
  private String accessToken;
  private String refreshToken;
  private LocalDateTime registeredDate;
}
