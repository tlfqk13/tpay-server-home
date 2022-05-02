package com.tpay.commons.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public enum DisappearDate {
    DISAPPEAR_DATE(LocalDateTime.now().minusYears(5).plusMonths(1).minusDays(1));

    private LocalDateTime disappearDate;

    DisappearDate(LocalDateTime disappearDate) {
        this.disappearDate = disappearDate;
    }
}
