package com.tpay.domains.point.application.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointFindResponse {
  private LocalDate startDate;
  private LocalDate endDate;
  private List<PointInfo> pointInfoList;
}
