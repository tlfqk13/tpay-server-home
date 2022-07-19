package com.tpay.domains.auth.application;

import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.jwt.TokenProvider;
import com.tpay.commons.jwt.TokenType;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.PushTokenService;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.domain.PushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.exception.ExceptionState.INVALID_PASSWORD;

@Service
@RequiredArgsConstructor
public class FranchiseeSignInService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final UserPushTokenService userPushTokenService;
    private final PushTokenService pushTokenService;

    private final TokenProvider provider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public SignInTokenInfo signIn(String businessNumber, String password, String pushToken) {
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByBusinessNumber(businessNumber);
        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

        if (!passwordEncoder.matches(
            password, franchiseeEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid Password");
        }

        AuthToken accessToken = authService.createAccessToken(franchiseeEntity);
        AuthToken refreshToken = authService.createRefreshToken(franchiseeEntity);
        authService.updateOrSave(franchiseeEntity, refreshToken.getValue());

        if(!(pushToken == null)) {
            // 토큰 테이블에 토큰 저장
            PushTokenEntity pushTokenEntity = new PushTokenEntity(pushToken);
            PushTokenEntity findPushTokenEntity = pushTokenService.saveIfNotExists(pushTokenEntity);

            // 유저-토큰 테이블 세이브 (기존 데이터는 삭제)
            userPushTokenService.deleteIfExistsAndSave(franchiseeEntity, findPushTokenEntity);
        }

        return SignInTokenInfo.builder()
            .franchiseeIndex(franchiseeEntity.getId())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
            .rejectReason(franchiseeApplicantEntity.getRejectReason())
            .accessToken(accessToken.getValue())
            .refreshToken(refreshToken.getValue())
            .popUp(franchiseeEntity.isPopUp())
            .signUpDate(franchiseeEntity.getCreatedDate())
            .isActiveSound(franchiseeEntity.getIsActiveSound())
            .isActiveVibration(franchiseeEntity.getIsActiveVibration())
            .storeName(franchiseeEntity.getStoreName())
            .isConnectedPos(franchiseeEntity.getIsConnectedPos())
            .posType(franchiseeEntity.getPosType().getPosName())
            .userSelector(UserSelector.FRANCHISEE)
            .build();
    }

    @Transactional
    public SignInTokenInfo signInNew(String businessNumber, String password, String pushToken) {
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByBusinessNumber(businessNumber);
        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(franchiseeEntity.getBusinessNumber(), password);

        Authentication auth = null;
        try {
            auth = authenticationManagerBuilder.getObject().authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BadCredentialsException e) {
            throw new InvalidPasswordException(INVALID_PASSWORD, "패스워드가 일치하지 않습니다");
        }

        String accessToken = provider.createToken(auth, TokenType.ACCESS_TOKEN);
        String refreshToken = provider.createToken(auth, TokenType.REFRESH_TOKEN);

        authService.updateOrSave(franchiseeEntity, refreshToken);

        if(!(pushToken == null)) {
            // 토큰 테이블에 토큰 저장
            PushTokenEntity pushTokenEntity = new PushTokenEntity(pushToken);
            PushTokenEntity findPushTokenEntity = pushTokenService.saveIfNotExists(pushTokenEntity);

            // 유저-토큰 테이블 세이브 (기존 데이터는 삭제)
            userPushTokenService.deleteIfExistsAndSave(franchiseeEntity, findPushTokenEntity);
        }

        return SignInTokenInfo.builder()
                .franchiseeIndex(franchiseeEntity.getId())
                .businessNumber(franchiseeEntity.getBusinessNumber())
                .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
                .rejectReason(franchiseeApplicantEntity.getRejectReason())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .popUp(franchiseeEntity.isPopUp())
                .signUpDate(franchiseeEntity.getCreatedDate())
                .isActiveSound(franchiseeEntity.getIsActiveSound())
                .isActiveVibration(franchiseeEntity.getIsActiveVibration())
                .storeName(franchiseeEntity.getStoreName())
                .isConnectedPos(franchiseeEntity.getIsConnectedPos())
                .posType(franchiseeEntity.getPosType().getPosName())
                .userSelector(UserSelector.FRANCHISEE)
                .build();
    }
}
