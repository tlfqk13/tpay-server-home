package com.tpay.commons.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("accessToken", 60 * 24),
    REFRESH_TOKEN("refreshToken", 60 * 24 * 7);

    private String name;
    private long expiredMinutes;

    public static long getExpiredMinutes(String tokenName) {
        return Arrays.stream(TokenType.values())
            .filter(tokenType -> tokenType.getName().equals(tokenName))
            .findAny()
            .get()
            .expiredMinutes;
    }
}
