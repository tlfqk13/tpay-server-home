package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.tpay.commons.push.detail.PushTopic;
import com.tpay.domains.push.application.dto.TopicDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicSubscribeService {
    public void subscribe(TopicDto.Request request) {
        this.subscribeRequest(request.getTokens(), request.getPushTopic());
    }


    public void subscribe(List<String> token, PushTopic pushTopic) {
        this.subscribeRequest(token, pushTopic);
    }

    public void subscribeRequest(List<String> token, PushTopic pushTopic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(token, pushTopic.toString());
            System.out.println(response.getSuccessCount() + "token were subscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
