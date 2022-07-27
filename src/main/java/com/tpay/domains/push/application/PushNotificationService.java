package com.tpay.domains.push.application;

import com.google.firebase.messaging.*;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushHistoryService pushHistoryService;
    private final UserPushTokenService userPushTokenService;

    @Transactional
    public void sendMessageByToken(NotificationDto.Request request) {
        try {
            Notification notification = createNotification(request);

            Message.Builder messageBuilder = Message.builder();
            addData(messageBuilder, request);
            messageBuilder.setNotification(notification)
                    .setToken(request.getPushTypeValue());

            Message message = messageBuilder.build();

            Optional<UserPushTokenEntity> optionalUserPushTokenEntity = userPushTokenService.findByToken(request.getPushTypeValue());
            if (optionalUserPushTokenEntity.isPresent()) {
                String send = FirebaseMessaging.getInstance().send(message);
                pushHistoryService.saveHistory(request, send, optionalUserPushTokenEntity.get(), 0L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public String sendMessageByTopic(NotificationDto.Request request) {
        try {
            Notification notification = createNotification(request);

            Message.Builder messageBuilder = Message.builder();
            addData(messageBuilder, request);
            messageBuilder.setNotification(notification)
                    .setTopic(request.getPushTypeValue())
                    .build();

            Message message = messageBuilder.build();

            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Transactional
    public void sendMessageByTokenWithoutNotification(NotificationDto.Request request) {
        try {
            Message.Builder messageBuilder = Message.builder();
            addData(messageBuilder, request);
            addHighPriority(messageBuilder);
            messageBuilder.setToken(request.getPushTypeValue());
            Message message = messageBuilder.build();
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Notification createNotification(NotificationDto.Request request) {
        return Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();
    }

    private void addData(Message.Builder builder, NotificationDto.Request request) {
        builder.putData("pushCategory", request.getPushCategory())
                .putData("link", request.getLink());
    }

    private void addHighPriority(Message.Builder builder) {
        builder.setApnsConfig(
                ApnsConfig.builder()
                        .putHeader("apns-priority", "5")
                        .build()
        )
                .setAndroidConfig(
                        AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .build()
                )
                .setWebpushConfig(
                        WebpushConfig.builder()
                                .putHeader("Urgency", "high")
                                .build()
                );
    }
}

