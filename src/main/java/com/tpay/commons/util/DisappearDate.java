package com.tpay.commons.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public enum DisappearDate {
    DISAPPEAR_DATE(LocalDateTime.now().minusYears(5).plusMonths(1).minusDays(1)),
    DISAPPEAR_ALERT_DATE(LocalDateTime.now().minusYears(5).minusDays(1));

    private LocalDateTime disappearDate;

    // TODO: 2022/03/18 -5y+1m-1d 기준보다 작거나 같은 데이터 모두 날려야합니다.
    DisappearDate(LocalDateTime disappearDate) {
        this.disappearDate = disappearDate;
    }
}
