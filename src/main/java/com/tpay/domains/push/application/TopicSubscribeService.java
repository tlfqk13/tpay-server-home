package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.push.domain.SubscribeType;
import com.tpay.domains.push.domain.TopicType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicSubscribeService {

    private final UserPushTokenService userPushTokenService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<String> subscribeByTopic(TopicType topic, SubscribeType subscribeType) {
        List<String> token = userPushTokenService.findAllToken();
        if (!token.isEmpty()) {
            try {
                if (subscribeType.equals(SubscribeType.SUBSCRIBE)) {
                    TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(token, topic.toString());
                    logger.trace("  SUBSCRIBE [topic : {}] [success,fail] : [{},{}] Error : [{}]", topic, response.getSuccessCount(), response.getFailureCount(), response.getErrors());
                    System.out.println(response.getSuccessCount() + "token were subscribed successfully");
                } else if (subscribeType.equals(SubscribeType.UNSUBSCRIBE)) {
                    TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(token, topic.toString());
                    logger.trace("UNSUBSCRIBE [topic : {}] [success,fail] : [{},{}] Error : [{}]", topic, response.getSuccessCount(), response.getFailureCount(), response.getErrors());
                }
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    public List<String> subscribeByFranchisee(List<FranchiseeEntity> franchiseeEntityList, TopicType topic, SubscribeType subscribeType) {

        List<String> token = userPushTokenService.findTokenByFranchiseeEntityList(franchiseeEntityList);
        if (token.isEmpty()) {
            // FranchiseeEntity 목록이 user_push_token 테이블에 하나도 맵핑되지 않았을 때.
            logger.trace(" SUBSCRIBE [ANY PUSH TOKEN MATCHED]");
            return new ArrayList<>();
        }

        try {
            if (subscribeType.equals(SubscribeType.SUBSCRIBE)) {
                TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(token, topic.toString());
                logger.trace("  SUBSCRIBE [topic : {}] [success,fail] : [{},{}] Error : [{}]", topic, response.getSuccessCount(), response.getFailureCount(), response.getErrors());
            } else if (subscribeType.equals(SubscribeType.UNSUBSCRIBE)) {
                TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(token, topic.toString());
                logger.trace("UNSUBSCRIBE [topic : {}] [success,fail] : [{},{}] Error : [{}]", topic, response.getSuccessCount(), response.getFailureCount(), response.getErrors());
            }
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return token;

    }
}
