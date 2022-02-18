package com.tpay.commons.util;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum DateFilter {
  TODAY(LocalDate.now(), LocalDate.now().plusDays(1)),
  WEEK(LocalDate.now().minusDays(7), LocalDate.now().plusDays(1)),
  MONTH(LocalDate.now().minusMonths(1), LocalDate.now().plusDays(1)),
  YEAR(LocalDate.now().minusYears(1), LocalDate.now().plusDays(1)),
  ALL,
  CUSTOM;

  private LocalDate startDate;
  private LocalDate endDate;

  DateFilter(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
