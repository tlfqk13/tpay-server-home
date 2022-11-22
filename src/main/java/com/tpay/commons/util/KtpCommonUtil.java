package com.tpay.commons.util;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import io.jsonwebtoken.Claims;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

public class KtpCommonUtil {

    private static final LocalDateTime APPLICATION_START_TIME = LocalDateTime.now();
    private static final int ONE_MINUTE = 60;

    // 어플리케이션 시작 시, @Schedule 작업이 시작되는 것을 방지하기 위해 조건으로 걸어줌
    public static boolean isApplicationInitBeforeOneMinute() {
        return Duration.between(APPLICATION_START_TIME, LocalDateTime.now()).getSeconds() < ONE_MINUTE;
    }

    public static IndexInfo getIndexInfoFromAccessToken(Claims claims) {
        Object accessE = claims.get("accessE");
        if (null == accessE) {
            Object accessF = claims.get("accessF");
            if (null != accessF) {
                return new IndexInfo(FRANCHISEE, String.valueOf(accessF));
            }
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Token doesn't have valid user info");
        }
        return new IndexInfo(EMPLOYEE, String.valueOf(accessE));
    }

    public static IndexInfo getIndexInfoFromRefreshToken(Claims claims) {
        Object accessE = claims.get("refreshE");
        if (null == accessE) {
            Object accessF = claims.get("refreshF");
            if (null != accessF) {
                return new IndexInfo(FRANCHISEE, String.valueOf(accessF));
            }
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Token doesn't have valid user info");
        }
        return new IndexInfo(EMPLOYEE, String.valueOf(accessE));
    }

}
