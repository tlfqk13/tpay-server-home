package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicSubscribeService {

    private final UserPushTokenService userPushTokenService;

    public List<String> subscribe(String topic) {
        List<String> token = userPushTokenService.findToken(topic);
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(token, topic);
            System.out.println(response.getSuccessCount() + "token were subscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return token;
    }

    public List<String> unsubscribe(String topic) {
        List<String> token = userPushTokenService.findToken(topic);
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(token, topic);
            System.out.println(response.getSuccessCount() + "token were unsubscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return token;
    }
}
