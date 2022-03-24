package com.tpay.commons.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
public enum DateFilterV2 {

    TODAY(LocalDate.now().toString(), LocalDate.now().plusDays(1).toString()),
    WEEK(LocalDate.now().minusDays(7).toString(), LocalDate.now().plusDays(1).toString()),
    MONTH(LocalDate.now().minusMonths(1).toString(), LocalDate.now().plusDays(1).toString()),
    YEAR(LocalDate.now().minusYears(1).toString(), LocalDate.now().plusDays(1).toString()),
    ALL(LocalDate.now().minusYears(100).toString(), LocalDate.now().plusYears(100).toString()),
    CUSTOM;

    private String startDate;
    private String endDate;


    DateFilterV2(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
