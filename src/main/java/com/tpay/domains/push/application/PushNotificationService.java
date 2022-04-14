package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PushNotificationService {

    private final PushHistoryRepository pushHistoryRepository;

    @Transactional
    public void sendMessageByToken(NotificationDto.Request request) {
        try {
            Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

            Message message = Message.builder()
                .setNotification(notification)
                .putData("pushCategory", request.getPushCategory())
                .putData("link", request.getLink())
                .setToken(request.getPushTypeValue())
                .build();
            String send = FirebaseMessaging.getInstance().send(message);

            PushHistoryEntity pushHistoryEntity = PushHistoryEntity.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .pushCategory(request.getPushCategory())
                .link(request.getLink())
                .pushType("token")
                .pushTypeValue(request.getPushTypeValue())
                .response(send)
                .build();

            pushHistoryRepository.save(pushHistoryEntity);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }


}

