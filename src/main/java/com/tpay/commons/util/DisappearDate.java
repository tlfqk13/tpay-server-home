package com.tpay.commons.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public enum DisappearDate {
  DISAPPEAR_DATE(LocalDate.now().minusYears(5));

  private LocalDate disappearDate;

  DisappearDate(LocalDate disappearDate) {
    this.disappearDate = disappearDate;
  }
}
