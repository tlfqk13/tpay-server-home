package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.PushTokenService;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.domain.PushTokenEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FranchiseeSignInService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final UserPushTokenService userPushTokenService;
    private final PushTokenService pushTokenService;

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
        authService.updateOrSaveRefreshToken(franchiseeEntity, refreshToken.getValue());
        authService.updateOrSaveAccessToken(franchiseeEntity,accessToken.getValue());

        if(!(pushToken == null)) {
            // 토큰 테이블에 토큰 저장
            PushTokenEntity pushTokenEntity = new PushTokenEntity(pushToken);
            PushTokenEntity findPushTokenEntity = pushTokenService.saveIfNotExists(pushTokenEntity);

            // 유저-토큰 테이블 세이브 (기존 데이터는 삭제)
            userPushTokenService.deleteIfExistsAndSave(franchiseeEntity, findPushTokenEntity);
        }

        log.trace("==========================로그인===========================");
        log.trace("[사업자번호] : {}", franchiseeEntity.getBusinessNumber());
        log.trace("[사업자번호] : {}", franchiseeEntity.getStoreName());
        log.trace("==========================로그인===========================");

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
            .balancePercentage(franchiseeEntity.getBalancePercentage())
            .storeName(franchiseeEntity.getStoreName())
            .isConnectedPos(franchiseeEntity.getIsConnectedPos())
            .posType(franchiseeEntity.getPosType().getPosName())
            .userSelector(UserSelector.FRANCHISEE)
            .build();
    }
}
