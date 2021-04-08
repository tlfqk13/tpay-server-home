package com.tpay.domains.point.application.dto;

import lombok.Getter;

@Getter
public class PointRequest {
  private Long franchiseeId;
  private int week;
  private int month;
}
