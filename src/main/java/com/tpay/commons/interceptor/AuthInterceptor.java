package com.tpay.commons.interceptor;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.FranchiseeAuthenticationException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(
        HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler)
        throws Exception {

        Optional<String> token = parseToken(servletRequest);
        if (token.isEmpty()) {
            throw new FranchiseeAuthenticationException(
                ExceptionState.AUTHENTICATION_FAILED, "Token not exists");
        }

        AuthToken authToken = jwtUtils.convertAuthToken(token.get());
        authToken.validate();
        return true;
    }

    private Optional<String> parseToken(HttpServletRequest request) {
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authToken)) {
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }
}
