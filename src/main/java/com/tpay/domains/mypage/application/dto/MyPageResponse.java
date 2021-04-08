package com.tpay.domains.mypage.application.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyPageResponse {
  private String storeName;
  private LocalDateTime createdDate;
  private Long totalSalesAmount;
  private Long totalPoint;
}
