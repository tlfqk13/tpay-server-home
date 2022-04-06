package com.tpay.domains.push.test.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.tpay.domains.push.test.application.dto.TopicDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushTopicService {
    public void subscribeToTopic(TopicDto.Request request) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(request.getTokens(), request.getTopic());
            System.out.println(response.getSuccessCount() + "token were subscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribeFromTopic(TopicDto.Request request) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(request.getTokens(), request.getTopic());
            System.out.println(response.getSuccessCount() + "token were unsubscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
