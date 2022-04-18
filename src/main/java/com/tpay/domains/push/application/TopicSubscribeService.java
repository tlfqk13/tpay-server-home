package com.tpay.domains.push.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.push.domain.SubscribeType;
import com.tpay.domains.push.domain.TopicType;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicSubscribeService {

    private final UserPushTokenService userPushTokenService;

    public List<String> subscribeByTopic(TopicType topic, SubscribeType subscribeType) {
        List<String> token = userPushTokenService.findTokenByTopic(topic);
        try {
            if (subscribeType.equals(SubscribeType.SUBSCRIBE)) {
                TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(token, topic.toString());
                System.out.println(response.getSuccessCount() + "token were subscribed successfully");
            } else if (subscribeType.equals(SubscribeType.UNSUBSCRIBE)) {
                TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(token, topic.toString());
                System.out.println(response.getSuccessCount() + "token were unsubscribed successfully");
            }
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return token;
    }

    public List<String> subscribeByFranchiseeApplicant(List<FranchiseeApplicantEntity> franchiseeApplicantEntityList, TopicType topic, SubscribeType subscribeType) {
        List<String> token = new ArrayList<>();
        for (FranchiseeApplicantEntity franchiseeApplicantEntity : franchiseeApplicantEntityList) {
            Optional<UserPushTokenEntity> optionalByFranchiseeIndex = userPushTokenService.findOptionalByFranchiseeIndex(franchiseeApplicantEntity.getFranchiseeEntity().getId());
            optionalByFranchiseeIndex.ifPresent(userPushTokenEntity -> token.add(userPushTokenEntity.getUserToken()));
        }

        try {
            if (subscribeType.equals(SubscribeType.SUBSCRIBE)) {
                TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(token, topic.toString());
                System.out.println(response.getSuccessCount() + "token were subscribed successfully(Applicant Entity)");
            } else if (subscribeType.equals(SubscribeType.UNSUBSCRIBE)) {
                TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(token, topic.toString());
                System.out.println(response.getSuccessCount() + "token were subscribed successfully(Applicant Entity)");
            }
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return token;
    }
}
