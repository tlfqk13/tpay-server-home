package com.tpay.commons.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class KtpCommonUtil {

    private static final LocalDateTime APPLICATION_START_TIME = LocalDateTime.now();
    private static final int ONE_MINUTE = 60;

    // 어플리케이션 시작 시, @Schedule 작업이 시작되는 것을 방지하기 위해 조건으로 걸어줌
    public static boolean isApplicationInitBeforeOneMinute() {
        return Duration.between(APPLICATION_START_TIME, LocalDateTime.now()).getSeconds() < ONE_MINUTE;
    }
}
