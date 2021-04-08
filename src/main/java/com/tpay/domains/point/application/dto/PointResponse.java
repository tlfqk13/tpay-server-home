package com.tpay.domains.point.application.dto;

import com.tpay.domains.point.domain.PointStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointResponse {
  private String datetime;
  private Long value;
  private PointStatus pointStatus;
}
