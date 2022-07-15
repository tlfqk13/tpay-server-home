package com.tpay.commons.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DisappearDate {
    public static LocalDateTime getDisappearDate(){
        return LocalDateTime.now().minusYears(5).plusMonths(1).minusDays(1);
    }
}
