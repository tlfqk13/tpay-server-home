package com.tpay.commons.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public enum DateFilter {
    TODAY,
    WEEK,
    MONTH,
    YEAR,
    CUSTOM;

    public LocalDate getStartDate() {
        LocalDate now = LocalDate.now();
        DateFilter curDateFilter = valueOf(this.name());

        if (DateFilter.TODAY == curDateFilter) {
            return now;
        } else if (DateFilter.WEEK == curDateFilter) {
            return now.minusDays(7);
        } else if (DateFilter.MONTH == curDateFilter) {
            return now.minusMonths(1);
        } else if (DateFilter.YEAR == curDateFilter) {
            return now.minusYears(1);
        } else {
            return LocalDate.MIN;
        }
    }

    public LocalDate getEndDate() {
        return CUSTOM != valueOf(this.name()) ? LocalDate.now().plusDays(1) : LocalDate.MAX;
    }
}
