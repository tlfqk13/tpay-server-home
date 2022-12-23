package com.tpay.commons.interceptor;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.FranchiseeAuthenticationException;
import com.tpay.commons.exception.detail.FranchiseeNotFoundException;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.KtpCommonUtil;
import com.tpay.domains.auth.application.AccessTokenService;
import com.tpay.domains.auth.domain.EmployeeAccessTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenEntity;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.tpay.commons.util.UserSelector.FRANCHISEE;

/**
 * 각 인증이 필요한 요청이 Jwt 토큰의 ID와 일치하는지, URI 별 검증
 * 생성 배경 : 올바른 accessToken 만 제공된다면, 타인의 정보까지 호출이 가능한 상황
 * K1 프로젝트 이후 URI 변경 또는 기능추가가 많이 없을 것으로 예상
 * ====용어정리====
 * [firstDomain] - URI의 가장 첫 도메인, 해당 도메인을 기준으로 개별 파싱
 * <p>
 * [trim] - 개별 파싱의 가장 첫 도메인을 잘라낸 나머지 문자열
 * <p>
 * [NstDomainEnd] - substring하기 위해 만들어짐
 * - n번째 도메인의 끝+1 위치. 어디서부터 카운트할지는 각각 다름. 단 변수명은 전체 도메인 기준으로 함. 아래 예시 참고
 * 예시) /franchisee/example/test 라는 URI에서 secondDomainEnd 는 example의 e부터 test의 t앞의 '/' 까지의 index를 센다.
 * <p>
 * [NstTrim] - 전체 URI 기준으로 n번째 도메인까지 잘라낸 나머지 문자열
 * ================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final EmployeeFindService employeeFindService;
    private final FranchiseeFindService franchiseeFindService;
    private final AccessTokenService accessTokenService;

    // 인터셉터 프리핸들 메서드 오버라이드
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (hasToken(request) && validateToken(request)) {
            AuthToken authToken = getAuthToken(request);
            IndexInfo tokenInfo = KtpCommonUtil.getIndexInfoFromAccessToken(authToken.getData());
            return checkDuplicateSignIn(tokenInfo, authToken.getValue());
        }
        return false;
    }

    private boolean hasToken(HttpServletRequest request) {
        Optional<String> token = parseToken(request);
        if (token.isEmpty()) {
            throw new FranchiseeAuthenticationException(
                    ExceptionState.AUTHENTICATION_FAILED, "Token not exists");
        }
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

    boolean checkDuplicateSignIn(IndexInfo indexInfo, String tokenValue) {
        String lastAccessToken;
        if (FRANCHISEE == indexInfo.getUserSelector()) {
            FranchiseeAccessTokenEntity franchiseeAccessTokenEntity =
                    accessTokenService.findByFranchiseeId(indexInfo.getIndex())
                            .orElseThrow(() -> new FranchiseeNotFoundException(ExceptionState.ID_NOT_FOUND));
            lastAccessToken = franchiseeAccessTokenEntity.getAccessToken();
        } else {
            EmployeeAccessTokenEntity employeeAccessTokenEntity =
                    accessTokenService.findByEmployeeId(indexInfo.getIndex())
                            .orElseThrow(() -> new FranchiseeNotFoundException(ExceptionState.ID_NOT_FOUND));
            lastAccessToken = employeeAccessTokenEntity.getAccessToken();
        }

        if (isDifferentTokenValue(tokenValue, lastAccessToken)) {
            log.debug("authToken = {}, latest = {} ", tokenValue, lastAccessToken);
            throw new JwtRuntimeException(ExceptionState.DUPLICATE_SIGNOUT);
        } else {
            return true;
        }
    }

    private boolean validateToken(HttpServletRequest request) {
        log.debug("URI = {}", request.getRequestURI());
        log.debug("Header - Authentication = {}", request.getHeader(HttpHeaders.AUTHORIZATION));
        log.trace("Duplicate _ Validation Check Start");

        AuthToken authToken = getAuthToken(request);
        return authToken.validate();
    }

    private AuthToken getAuthToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return jwtUtils.convertAuthToken(header);
    }

    private boolean isDifferentTokenValue(String tokenFromRequest, String tokenFromDB) {
        return !tokenFromRequest.equals(tokenFromDB);
    }
}


