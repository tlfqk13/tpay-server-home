package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.tpay.domains.push.application.dto.TopicDto;
import org.springframework.stereotype.Service;

@Service
public class TopicSubscribeService {
    public void subscribe(TopicDto.Request request) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(request.getTokens(), request.getTopic());
            System.out.println(response.getSuccessCount() + "token were subscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
