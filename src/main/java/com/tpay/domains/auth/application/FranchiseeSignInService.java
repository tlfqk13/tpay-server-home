package com.tpay.domains.auth.application;

import com.tpay.commons.jwt.AuthToken;
import com.tpay.domains.auth.application.dto.FranchiseeTokenInfo;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.PushUpdateService;
import com.tpay.domains.push.domain.PushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeSignInService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final PushUpdateService pushUpdateService;



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
        PushTokenEntity pushTokenEntity = PushTokenEntity.builder()
            .pushToken(pushToken)
            .franchiseeEntity(franchiseeEntity)
            .build();
        pushUpdateService.updateOrSaveByFranchiseeIndex(pushTokenEntity);

        return FranchiseeTokenInfo.builder()
            .franchiseeIndex(franchiseeEntity.getId())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
            .rejectReason(franchiseeApplicantEntity.getRejectReason())
            .accessToken(accessToken.getValue())
            .refreshToken(refreshToken.getValue())
            .popUp(franchiseeEntity.isPopUp())
            .signUpDate(franchiseeEntity.getCreatedDate())
            .build();
    }
}
