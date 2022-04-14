package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.tpay.domains.push.application.dto.TopicDto;
import org.springframework.stereotype.Service;

@Service
public class TopicUnsubscribeService {

    public void unsubscribe(TopicDto.Request request) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(request.getTokens(), request.getPushTopic().toString());
            System.out.println(response.getSuccessCount() + "token were unsubscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
