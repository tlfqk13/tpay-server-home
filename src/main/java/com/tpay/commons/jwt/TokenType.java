package com.tpay.commons.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("accessToken", 24 * 7 * 4 * 6),
    REFRESH_TOKEN("refreshToken", 24 * 7 * 4 * 6);

    private String name;
    private long expiredHours;

    public static long getExpiredHours(String tokenName) {
        return Arrays.stream(TokenType.values())
            .filter(tokenType -> tokenType.getName().equals(tokenName))
            .findAny()
            .get()
            .expiredHours;
    }
}
