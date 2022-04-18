package com.tpay.domains.push.application;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.domains.push.application.dto.AdminNotificationDto;
import com.tpay.domains.push.application.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPushService {

    private final TopicSubscribeService topicSubscribeService;
    private final PushNotificationService pushNotificationService;
    private final PushHistorySaveService pushHistorySaveService;

    private final UserPushTokenService userPushTokenService;

    @Transactional
    public AdminNotificationDto.Response sendMessageByAdmin(AdminNotificationDto.Request adminRequest) {
        String topic = "ALL";
        List<String> subscribeList = topicSubscribeService.subscribe(topic);
        NotificationDto.Request request = new NotificationDto.Request(PushCategoryType.CASE_FIFTEEN, PushType.TOPIC, topic, adminRequest.getTitle(), adminRequest.getBody());
        String send = pushNotificationService.sendMessageByTopic(request);
        topicSubscribeService.unsubscribe(topic);
        subscribeList.stream().map(userPushTokenService::findByToken).forEach(entity -> pushHistorySaveService.saveHistory(request, send, entity));
        return AdminNotificationDto.Response.builder()
            .message(send)
            .build();
    }
}
