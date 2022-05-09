package com.tpay.commons.interceptor;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.util.UserSelector;
import io.jsonwebtoken.Claims;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        return validationCheck(request);
    }

    private boolean validationCheck(HttpServletRequest request) {

        Claims claims = getClaims(request);
        IndexInfo indexInfo = getIndexFromClaims(claims);
        IndexInfo indexFromUri = getIndexFromUri(request);

        if (indexInfo.getUserSelector().equals(indexFromUri.userSelector) && indexInfo.getIndex().equals(indexFromUri.getIndex())) {
            return true;
        } else {
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "MUST EQUALS!!!");
        }

    }

    private Claims getClaims(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        AuthToken authToken = jwtUtils.convertAuthToken(header);
        return authToken.getData();
    }

    private IndexInfo getIndexFromClaims(Claims claims) {
        Object accessE = claims.get("accessE");
        if (accessE == null) {
            Object accessF = claims.get("accessF");
            log.trace("@@@@@ accessF = {}", accessF);
            return new IndexInfo(UserSelector.FRANCHISEE, String.valueOf(accessF));
        }
        log.trace("@@@@@ accessE = {}", accessE);
        return new IndexInfo(UserSelector.EMPLOYEE, String.valueOf(accessE));
    }

    private IndexInfo getIndexFromUri(HttpServletRequest request) {
        String firstDomain = getFirstDomain(request);
        IndexInfo indexInfo = new IndexInfo();
        if (firstDomain.equals(UriType.EMPLOYEE.getKeyword())) {
            indexInfo = getIndexFromUriEmployee(request);
        } else if (firstDomain.equals(UriType.FRANCHISEE.getKeyword())) {
            indexInfo = getIndexFromUriFranchisee(request);
        }
        log.trace("@@@@@@@@@@@@{} {}", indexInfo.getUserSelector(), indexInfo.getIndex());
        return indexInfo;
    }

    private String getFirstDomain(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        int i = requestURI.indexOf("/", 1);
        return requestURI.substring(1, i);
    }


    //이 아래는 URI 추가, 변경되면 체크해야 하는 곳. 각 개별로직이 들어가있음
    private IndexInfo getIndexFromUriEmployee(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(10);
        int secondDomainIndex = trim.indexOf("/");
        if (secondDomainIndex == -1) {
            return new IndexInfo(UserSelector.FRANCHISEE, trim);
        } else {
            return new IndexInfo(UserSelector.EMPLOYEE, trim.substring(0, secondDomainIndex));
        }
    }

    private IndexInfo getIndexFromUriFranchisee(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(12);
        int secondDomainIndex = trim.indexOf("/");
        if (secondDomainIndex == -1) {

        } else if (trim.substring(0, secondDomainIndex).equals(UriType.FRANCHISEE_PASSWORD.getKeyword())) {
            String secondTrim = request.getRequestURI().substring(21);
            int thirdDomainIndex = secondTrim.indexOf("/");
            String index = secondTrim.substring(thirdDomainIndex + 1);
            return new IndexInfo(UserSelector.FRANCHISEE, index);
        }
        // TODO: 2022/05/09 이어서
        return new IndexInfo();
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class IndexInfo {
        private UserSelector userSelector;
        private String index;
    }


    @Getter
    @AllArgsConstructor
    private enum UriType {
        EMPLOYEE("employee"),
        FRANCHISEE("franchisee"),
        FRANCHISEE_PASSWORD("password"),
        FRANCHISEE_EQUALS("equals"),
        FRANCHISEE_IN("in");

        private String keyword;
    }
}


