package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.tpay.commons.push.detail.PushTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicSubscribeService {

    public void subscribe(List<String> token, PushTopic pushTopic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(token, pushTopic.toString());
            System.out.println(response.getSuccessCount() + "token were subscribed successfully");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(List<String> token, PushTopic pushTopic){
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(token, pushTopic.toString());
            System.out.println(response.getSuccessCount() + "token were unsubscribed successfully" );
        } catch (FirebaseMessagingException e){
            e.printStackTrace();
        }
    }

}
