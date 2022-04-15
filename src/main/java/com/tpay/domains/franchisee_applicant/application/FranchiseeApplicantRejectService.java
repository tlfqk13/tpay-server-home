package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.application.PushNotificationService;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.push.PushType.TOKEN;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantRejectService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final PushNotificationService pushNotificationService;
    private final UserPushTokenService userPushTokenService;

    @Transactional
    public void reject(Long franchiseeApplicantIndex, String rejectReason) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);

        UserPushTokenEntity userPushTokenEntity = userPushTokenService.findByFranchiseeIndex(franchiseeApplicantEntity.getFranchiseeEntity().getId());
        NotificationDto.Request request = new NotificationDto.Request(PushCategoryType.CASE_THREE, TOKEN, userPushTokenEntity.getUserToken());
        NotificationDto.Request requestSetFront = request.setFrontTitle(franchiseeApplicantEntity.getFranchiseeEntity().getStoreName());
        pushNotificationService.sendMessageByToken(requestSetFront);
        franchiseeApplicantEntity.reject(rejectReason);
    }
}
