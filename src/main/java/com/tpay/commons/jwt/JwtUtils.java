package com.tpay.commons.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    @Value("${custom.jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

    public AuthToken createAuthToken(Map<String, Object> payload, TokenType tokenType) {
        addCommonPayload(payload, tokenType);
        return new AuthToken(payload, key);
    }

    public void addCommonPayload(Map<String, Object> payload, TokenType tokenType) {

        Date expiredDate = createExpiredDate(tokenType.getExpiredHours());
//        payload.put("sub", tokenType == TokenType.ACCESS_TOKEN ? payload.get("access") : payload.get("refresh"));
        payload.put("iat", new Date());
        payload.put("exp", expiredDate);
    }

    public AuthToken convertAuthToken(String token) {
        String bearerToken = token;
        if (token.contains("Bearer")) {
            bearerToken = token.substring(7);
        }
        log.trace("Auth Token Data : {} , {} ", bearerToken, key);
        return new AuthToken(bearerToken, key);
    }

    // TODO: 2022/09/07 자동 로그인 이슈 테스트 !!!!!!! * 테스트 반드시 수정
    public Date createExpiredDate(long minutes) {
        return Date.from(
                LocalDateTime.now().plusHours(minutes).atZone(ZoneId.systemDefault()).toInstant());
        //LocalDateTime.now().plusSeconds(60).atZone(ZoneId.systemDefault()).toInstant());
    }
}
