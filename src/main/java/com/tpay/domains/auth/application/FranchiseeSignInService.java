package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class FranchiseeSignInService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final UserPushTokenService userPushTokenService;

    @Transactional
    public FranchiseeTokenInfo signIn(String businessNumber, String password, String pushToken) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            franchiseeApplicantFindService.findByBusinessNumber(businessNumber);

        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

        if (!passwordEncoder.matches(
            password, franchiseeEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid Password");
        }

        AuthToken accessToken = authService.createAccessToken(franchiseeEntity);
        AuthToken refreshToken = authService.createRefreshToken(franchiseeEntity);
        authService.updateOrSave(franchiseeEntity, refreshToken.getValue());

        UserPushTokenEntity userPushTokenEntity = UserPushTokenEntity.builder()
            .userSelector(FRANCHISEE)
            .userId(franchiseeEntity.getId())
            .userToken(pushToken)
            .build();

        userPushTokenService.save(userPushTokenEntity);
        log.trace("[bizNo - {}] : [{} Save Successful] [UserToken - {}]", franchiseeEntity.getBusinessNumber(),userPushTokenEntity.getUserId(), userPushTokenEntity.getUserToken());

        return FranchiseeTokenInfo.builder()
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
            .build();
    }
}
