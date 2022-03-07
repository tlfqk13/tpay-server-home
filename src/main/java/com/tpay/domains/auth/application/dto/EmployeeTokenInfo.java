package com.tpay.domains.auth.application.dto;


import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
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
  private Long franchiseeIndex;
  private FranchiseeStatus franchiseeStatus;
}
