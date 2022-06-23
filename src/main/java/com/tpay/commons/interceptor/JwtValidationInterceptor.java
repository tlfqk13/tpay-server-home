package com.tpay.commons.interceptor;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import io.jsonwebtoken.Claims;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

/**
 * 각 인증이 필요한 요청이 Jwt 토큰의 ID와 일치하는지, URI 별 검증
 * 생성 배경 : 올바른 accessToken 만 제공된다면, 타인의 정보까지 호출이 가능한 상황
 * K1 프로젝트 이후 URI 변경 또는 기능추가가 많이 없을 것으로 예상
 * ====용어정리====
 * [firstDomain] - URI의 가장 첫 도메인, 해당 도메인을 기준으로 개별 파싱
 *
 * [trim] - 개별 파싱의 가장 첫 도메인을 잘라낸 나머지 문자열
 *
 * [NstDomainEnd] - substring하기 위해 만들어짐
 * - n번째 도메인의 끝+1 위치. 어디서부터 카운트할지는 각각 다름. 단 변수명은 전체 도메인 기준으로 함. 아래 예시 참고
 * 예시) /franchisee/example/test 라는 URI에서 secondDomainEnd 는 example의 e부터 test의 t앞의 '/' 까지의 index를 센다.
 *
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

    // 인터셉터 프리핸들 메서드 오버라이드
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        return validationCheck(request);
    }

    // 벨리데이션 체크는 헤더의 Authorization 의 jwt AT로 나온 index와 URI의 index를 비교하는 책임
    private boolean validationCheck(HttpServletRequest request) {

        Claims claims = getClaims(request);
        IndexInfo indexInfo = getIndexFromClaims(claims);
        IndexInfo indexFromUri = getIndexFromUri(request);

        if (indexInfo.getUserSelector().equals(indexFromUri.userSelector) && indexInfo.getIndex().equals(indexFromUri.getIndex())) {
            return true;
        } else if(indexInfo.getUserSelector().equals(EMPLOYEE) && indexFromUri.getUserSelector().equals(FRANCHISEE)) {
            Long id = employeeFindService.findById(Long.parseLong(indexInfo.getIndex())).get().getFranchiseeEntity().getId();
            String franchiseeIndexFromEmployee = String.valueOf(id);

            if(franchiseeIndexFromEmployee.equals(indexFromUri.getIndex())){
                return true;
            }
            log.warn("REQUEST URI : {}", request.getRequestURI());
            log.warn("INDEX FROM URI : {} {}", indexFromUri.getUserSelector(),indexFromUri.getIndex());
            log.warn("FRANCHISEE INDEX FROM EMPLOYEE : {}", franchiseeIndexFromEmployee);
            log.warn("INDEX FROM AUTH: {} {}", indexInfo.getUserSelector(), indexInfo.getIndex());
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "jwt A Error : Authorization & URIInfo mismatch");
        } else {
            log.warn("REQUEST URI : {}", request.getRequestURI());
            log.warn("INDEX FROM URI : {} {}", indexFromUri.getUserSelector(),indexFromUri.getIndex());
            log.warn("INDEX FROM AUTH: {} {}", indexInfo.getUserSelector(), indexInfo.getIndex());
            throw new JwtRuntimeException(ExceptionState.INVALID_TOKEN, "jwt B Error : Authorization & URIInfo mismatch");
        }

    }

    //jwt AT 토큰 파싱
    private IndexInfo getIndexFromClaims(Claims claims) {
        Object accessE = claims.get("accessE");
        if (accessE == null) {
            Object accessF = claims.get("accessF");
            return new IndexInfo(FRANCHISEE, String.valueOf(accessF));
        }
        return new IndexInfo(EMPLOYEE, String.valueOf(accessE));
    }

    //jwt AT 토큰 파싱
    private Claims getClaims(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        AuthToken authToken = jwtUtils.convertAuthToken(header);
        return authToken.getData();
    }

    // URI index 파싱 - 도메인별 개별작업
    private IndexInfo getIndexFromUri(HttpServletRequest request) {
        final String firstDomain = getFirstDomain(request);
        IndexInfo indexInfo = new IndexInfo();
        if (isEquals(firstDomain, UriType.FRANCHISEE)) {
            indexInfo = getIndexFromUriFranchisee(request);
        } else if (isEquals(firstDomain, UriType.POINTS)) {
            indexInfo = getIndexFromUriPoints(request);
        } else if (isEquals(firstDomain, UriType.SALE)) {
            indexInfo = getIndexFromUriSale(request);
        } else if (isEquals(firstDomain, UriType.EMPLOYEE)) {
            indexInfo = getIndexFromUriEmployee(request);
        } else if (isEquals(firstDomain, UriType.REFUNDS)) {
            indexInfo = getIndexFromUriRefunds(request);
        } else if (isEquals(firstDomain, UriType.REFUND_CORE)) {
            indexInfo = getIndexFromUriRefundCore(request);
        } else if (isEquals(firstDomain, UriType.POS)) {
            indexInfo = getIndexFromUriPos(request);
        } else if (isEquals(firstDomain, UriType.PUSH)) {
            indexInfo = getIndexFromUriPush(request);
        } else if (isEquals(firstDomain, UriType.FRANCHISEE_UPLOAD)) {
            indexInfo = getIndexFromUriFranchiseeUpload(request);
        } else if (isEquals(firstDomain, UriType.FRANCHISEE_APPLICANTS)) {
            indexInfo = getIndexFromUriFranchiseeApplicants(request);
        } else if (isEquals(firstDomain, UriType.TEST_FRANCHISEE_APPLICANT)) {
            indexInfo = getTest(request);
        }

        return indexInfo;
    }

    private boolean isEquals(String firstDomain, UriType uriType) {
        return firstDomain.equals(uriType.getKeyword());
    }

    // URI index 파싱
    private String getFirstDomain(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        int i = requestURI.indexOf("/", 1);
        return requestURI.substring(1, i);
    }


    //이 아래 부터는 URI 추가, 변경되면 체크해야 하는 곳. 각 개별로직이 들어가있음
    private IndexInfo getIndexFromUriEmployee(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(10);
        int secondDomainEnd = trim.indexOf("/");
        if (secondDomainEnd == -1) {
            return new IndexInfo(FRANCHISEE, trim);
        } else {
            return new IndexInfo(EMPLOYEE, trim.substring(0, secondDomainEnd));
        }
    }

    private IndexInfo getIndexFromUriFranchisee(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(12);
        int secondDomainEnd = trim.indexOf("/");
        if (secondDomainEnd == -1) {
            return new IndexInfo(FRANCHISEE, trim);
        } else if (trim.contains(UriType.FRANCHISEE_PASSWORD.getKeyword())) {
            int thirdDomainEnd = trim.indexOf("/", 9);
            String fourthTrim = trim.substring(thirdDomainEnd + 1);
            return new IndexInfo(FRANCHISEE, fourthTrim);
        } else if (trim.contains(UriType.FRANCHISEE_BANK.getKeyword())) {
            return new IndexInfo(FRANCHISEE, trim.substring(secondDomainEnd + 1));
        } else {
            return new IndexInfo(FRANCHISEE, trim.substring(0, secondDomainEnd));
        }
    }

    private IndexInfo getIndexFromUriFranchiseeApplicants(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(23);

        int secondDomainEnd = trim.indexOf("/");
        if (secondDomainEnd == -1) {
            if(request.getMethod().equals("GET")){
                FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByIndex(Long.parseLong(trim));
                return new IndexInfo(FRANCHISEE,franchiseeApplicantEntity.getFranchiseeEntity().getId().toString());
            } else {
                FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByBusinessNumber(trim);
                String index = franchiseeApplicantEntity.getFranchiseeEntity().getId().toString();
                return new IndexInfo(FRANCHISEE, index);
            }
        } else {
            String thirdTrim = request.getRequestURI().substring(31);
            return new IndexInfo(FRANCHISEE, thirdTrim);
        }
    }

    private IndexInfo getIndexFromUriFranchiseeUpload(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(18);
        return new IndexInfo(FRANCHISEE, trim);
    }

    private IndexInfo getIndexFromUriPoints(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(8);
        if (trim.contains(UriType.POINTS_TOTAL.getKeyword())) {
            int secondDomainEnd = trim.indexOf("/", 12);
            return new IndexInfo(FRANCHISEE, trim.substring(11, secondDomainEnd));
        } else {
            return new IndexInfo(FRANCHISEE, trim.substring(11));
        }
    }

    private IndexInfo getIndexFromUriPos(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(5);
        if (request.getRequestURI().contains(UriType.POS_LIMIT.getKeyword())) {
            return new IndexInfo(FRANCHISEE, trim.substring(13));
        } else {
            return new IndexInfo(FRANCHISEE, trim);
        }
    }

    private IndexInfo getIndexFromUriPush(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(6);
        if (trim.contains(UriType.PUSH_DETAIL.getKeyword())) {
            return new IndexInfo(FRANCHISEE, trim.substring(7));
        } else {
            return new IndexInfo(FRANCHISEE, trim);
        }
    }

    private IndexInfo getIndexFromUriRefunds(HttpServletRequest request) {
        int secondDomainEnd = request.getRequestURI().indexOf("/", 9);
        return new IndexInfo(FRANCHISEE, request.getRequestURI().substring(secondDomainEnd + 1));
    }

    private IndexInfo getIndexFromUriRefundCore(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(8);
        String secondTrim = "";
        if (trim.contains(UriType.REFUND_CORE_APPROVAL.getKeyword())) {
            secondTrim = trim.substring(9);
        } else if (trim.contains(UriType.REFUND_CORE_CANCEL.getKeyword())) {
            secondTrim = trim.substring(7);
        }
        int thirdDomainEnd = secondTrim.indexOf("/");
        String userSelectorString = secondTrim.substring(0, thirdDomainEnd);
        if (FRANCHISEE.toString().equals(userSelectorString)) {
            return new IndexInfo(FRANCHISEE, secondTrim.substring(thirdDomainEnd + 1));
        } else if (EMPLOYEE.toString().equals(userSelectorString)) {
            return new IndexInfo(EMPLOYEE, secondTrim.substring(thirdDomainEnd + 1));
        } else {
            return new IndexInfo();
        }
    }

    private IndexInfo getIndexFromUriSale(HttpServletRequest request) {
        String trim = request.getRequestURI().substring(7);
        if (trim.contains(UriType.SALE_DETAIL.getKeyword())) {
            String thirdTrim = trim.substring(18);
            return new IndexInfo(FRANCHISEE, thirdTrim);
        } else {
            int secondDomainEnd = trim.indexOf("/");
            String secondTrim = trim.substring(secondDomainEnd+1);
            return new IndexInfo(FRANCHISEE, secondTrim);
        }
    }

    private IndexInfo getTest(HttpServletRequest request) {
        // "/franchisee-applicant/{franchiseeApplicantIndex}" 하나가 testflight에 업데이트가 안되어 강제처리
        String trim = request.getRequestURI().substring(22);
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByIndex(Long.parseLong(trim));
        Long id = franchiseeApplicantEntity.getFranchiseeEntity().getId();
        return new IndexInfo(FRANCHISEE, id.toString());
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
        FRANCHISEE_IN("in"),
        FRANCHISEE_BANK("bank"),
        FRANCHISEE_UPLOAD("franchiseeUpload"),
        FRANCHISEE_APPLICANTS("franchisee-applicants"),
        TEST_FRANCHISEE_APPLICANT("franchisee-applicant"),
        POINTS("points"),
        POINTS_TOTAL("total"),
        POS("pos"),
        POS_LIMIT("limit"),
        PUSH("push"),
        PUSH_DETAIL("detail"),
        REFUNDS("refunds"),
        REFUND_CORE("refund"),
        REFUND_CORE_APPROVAL("approval"),
        REFUND_CORE_CANCEL("cancel"),
        SALE("sales"),
        SALE_DETAIL("detail");
        private String keyword;
    }
}


