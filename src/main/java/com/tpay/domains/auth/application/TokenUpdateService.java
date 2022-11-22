package com.tpay.domains.auth.application;

import com.tpay.commons.exception.detail.JwtRuntimeException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.JwtUtils;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.auth.application.dto.TokenRefreshVo;
import com.tpay.domains.auth.domain.EmployeeTokenEntity;
import com.tpay.domains.auth.domain.EmployeeTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeTokenRepository;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tpay.commons.util.KtpCommonUtil.getIndexInfoFromRefreshToken;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenUpdateService {
    private final FranchiseeTokenRepository franchiseeTokenRepository;
    private final EmployeeTokenRepository employeeTokenRepository;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    /**
     * @throws JwtRuntimeException : refresh token 인증과 관련된 에러메시지
     */
    @Transactional
    public SignInTokenInfo refresh(TokenRefreshVo.Request tokenInfo) {
        validateRefreshToken(tokenInfo.getRefreshToken());
        AuthToken authToken = jwtUtils.convertAuthToken(tokenInfo.getRefreshToken());
        IndexInfo indexInfo = getIndexInfoFromRefreshToken(authToken.getData());
        Long index = indexInfo.getIndex();
        AuthToken accessToken;
        if (FRANCHISEE == indexInfo.getUserSelector()) {
            FranchiseeTokenEntity franchiseeTokenEntity =
                    franchiseeTokenRepository.findByFranchiseeEntityId(index)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee"));
            franchiseeTokenEntity.validToken(tokenInfo.getRefreshToken());

            FranchiseeEntity franchiseeEntity = franchiseeTokenEntity.getFranchiseeEntity();
            accessToken = authService.createAccessToken(franchiseeEntity);
            authService.updateOrSaveAccessToken(franchiseeEntity, accessToken.getValue());
        } else {
            EmployeeTokenEntity employeeTokenEntity = employeeTokenRepository.findByEmployeeEntityId(index)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Employee Entity"));
            employeeTokenEntity.validToken(tokenInfo.getRefreshToken());

            EmployeeEntity employeeEntity = employeeTokenEntity.getEmployeeEntity();
            accessToken = authService.createAccessToken(employeeEntity);
            authService.updateOrSaveAccessToken(employeeEntity, accessToken.getValue());
        }

//        TODO: 프론트 uri 변경작업 진행 시 다시 진행
//        return new TokenRefreshVo(accessToken.getValue());

        return buildSignInInfo(indexInfo.getUserSelector(), accessToken.getValue(), tokenInfo);
    }

    private void validateRefreshToken(String refreshToken) {
        AuthToken authToken = jwtUtils.convertAuthToken(refreshToken);
        authToken.validate();
    }

    private SignInTokenInfo buildSignInInfo(UserSelector userSelector, String newAccessToken, TokenRefreshVo.Request tokenInfo) {
        return SignInTokenInfo.builder()
                .accessToken(newAccessToken)
                .refreshToken(tokenInfo.getRefreshToken())
                .userSelector(userSelector)
                .build();
    }
}
