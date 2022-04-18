package com.tpay.domains.batch.push_batch.application;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import com.tpay.domains.push.application.PushHistorySaveService;
import com.tpay.domains.push.application.PushNotificationService;
import com.tpay.domains.push.application.TopicSubscribeService;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.SubscribeType;
import com.tpay.domains.push.domain.TopicType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PushBatchService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final PushNotificationService pushNotificationService;
    private final TopicSubscribeService topicSubscribeService;
    private final UserPushTokenService userPushTokenService;
    private final PushHistorySaveService pushHistorySaveService;

    public void batchPush() {
        caseFour();
    }


    @Scheduled(cron = "0 1 16 * * *")
    private void caseFour() {
        TopicType topic = TopicType.REJECTED;
        List<FranchiseeApplicantEntity> franchiseeApplicantEntityList = franchiseeApplicantFindService.findByFranchiseeStatus(FranchiseeStatus.REJECTED);
        if (franchiseeApplicantEntityList.isEmpty()) {
            System.out.println("Nothing to Update - CASE_FOUR");
        } else {
            List<FranchiseeApplicantEntity> dateFilter = franchiseeApplicantEntityList.stream().filter(this::isAfterOneDate).collect(Collectors.toList());
            List<String> subscribeList = topicSubscribeService.subscribeByFranchiseeApplicant(dateFilter, topic, SubscribeType.SUBSCRIBE);
            NotificationDto.Request request = new NotificationDto.Request(PushCategoryType.CASE_FOUR, PushType.TOPIC, topic.toString());
            String send = pushNotificationService.sendMessageByTopic(request);
            topicSubscribeService.subscribeByFranchiseeApplicant(dateFilter, topic, SubscribeType.UNSUBSCRIBE);
            subscribeList.stream().map(userPushTokenService::findByToken).forEach(entity -> pushHistorySaveService.saveHistory(request, send, entity));
        }
    }


    private boolean isAfterOneDate(FranchiseeApplicantEntity franchiseeApplicantEntity) {
        LocalDateTime minusDays = LocalDateTime.now().minusDays(1);
        return franchiseeApplicantEntity.getCreatedDate().isBefore(minusDays);
    }
}
