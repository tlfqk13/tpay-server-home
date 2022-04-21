package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushHistoryService pushHistoryService;
    private final UserPushTokenService userPushTokenService;

    @Transactional
    public String sendMessageByToken(NotificationDto.Request request) {
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

            UserPushTokenEntity userPushTokenEntity = userPushTokenService.findByToken(request.getPushTypeValue());

            String send = FirebaseMessaging.getInstance().send(message);
            pushHistoryService.saveHistory(request,send,userPushTokenEntity);
            return send;

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Transactional
    public String sendMessageByTopic(NotificationDto.Request request) {
        try {
            Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

            Message message = Message.builder()
                .setNotification(notification)
                .putData("pushCategory", request.getPushCategory())
                .putData("link", request.getLink())
                .setTopic(request.getPushTypeValue())
                .build();

            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }


}

