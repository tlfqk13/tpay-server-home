package com.tpay.commons.jwt;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
@Profile(value = "security")
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";
    private final String secretKey;
    private Key key;

    public TokenProvider(@Value("${custom.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
    }

    public String createToken(Authentication auth, TokenType tokenType) {
        return createToken(auth, tokenType, null);
    }

    public String createToken(Authentication auth, TokenType tokenType, String userid) {
        String authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date validity = Date.from(LocalDateTime.now().plusHours(tokenType.getExpiredHours())
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setHeader(createHeaders())
                .setSubject(auth.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("userid", userid)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User user = new User(claims.getSubject(), "", authorities);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, token, authorities);
        if(null != claims.get("userid")) {
            authToken.setDetails(Map.of("userid", claims.get("userid")));
        }
        return authToken;
    }


    public boolean validateToken(String token) {
        try {
            Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            //                throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Token Data Empty");
            return !body.isEmpty();
        } catch (SecurityException e) {
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new JwtRuntimeException(
                    ExceptionState.INVALID_TOKEN, "JWT token compact of handler are invalid");
        }
    }

    private Map<String, Object> createHeaders() {
        return Map.of("typ", "JWT", "alg", "HS256");
    }
}
