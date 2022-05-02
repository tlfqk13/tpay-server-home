package com.tpay.domains.push.application;


import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


/**
 * 배치가 아닌 Push 들 메시징하는 서비스
 */
@Service
@RequiredArgsConstructor
public class NonBatchPushService {

    private final UserPushTokenService userPushTokenService;
    private final PushNotificationService pushNotificationService;

    // 토큰기반 Non-Batch 푸시 알람
    @Transactional
    public void nonBatchPushNSave(PushCategoryType pushCategoryType, Long franchiseeIndex) {
        Optional<UserPushTokenEntity> optionalUserPushTokenEntity = userPushTokenService.optionalFindByFranchiseeIndex(franchiseeIndex);
        if (optionalUserPushTokenEntity.isEmpty()) {
            return;
        }
        NotificationDto.Request request = new NotificationDto.Request(pushCategoryType, PushType.TOKEN, optionalUserPushTokenEntity.get().getPushTokenEntity().getToken());
        pushNotificationService.sendMessageByToken(request);
    }

    //Non-Batch 이면서 동적 메시지인 케이스 : 3, 7, 14
    @Transactional
    public void nonBatchPushNSave(PushCategoryType pushCategoryType, Long franchiseeIndex, String message) {
        Optional<UserPushTokenEntity> optionalUserPushTokenEntity = userPushTokenService.optionalFindByFranchiseeIndex(franchiseeIndex);
        if (optionalUserPushTokenEntity.isEmpty()) {
            return;
        }
        NotificationDto.Request request = new NotificationDto.Request(pushCategoryType, PushType.TOKEN, optionalUserPushTokenEntity.get().getPushTokenEntity().getToken());
        ;
        switch (pushCategoryType) {
            case CASE_THREE:
                request = request.setFrontTitle(message);
                break;
            case CASE_SEVEN:
                request = request.setBehindBodyPoint(message);
                break;
            case CASE_FOURTEEN:
                request = request.setFrontBody(message);
        }
        pushNotificationService.sendMessageByToken(request);
    }

}
