package com.tpay.domains.batch.push_batch.application;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.commons.util.DisappearDate;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
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

import static com.tpay.commons.push.PushCategoryType.*;

@Service
@RequiredArgsConstructor
public class PushBatchService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final PushNotificationService pushNotificationService;
    private final TopicSubscribeService topicSubscribeService;
    private final UserPushTokenService userPushTokenService;
    private final PushHistorySaveService pushHistorySaveService;
    private final PointRepository pointRepository;

    public void batchPush() {
        caseOne();
        caseFour();
        caseEight();
    }


    @Scheduled(cron = "0 0 14 * * *")
    public void caseOne() {
        TopicType topic = TopicType.INIT;
        List<FranchiseeApplicantEntity> franchiseeApplicantEntityList = franchiseeApplicantFindService.findByFranchiseeStatus(FranchiseeStatus.INIT);
        if (franchiseeApplicantEntityList.isEmpty()) {
            System.out.println("Nothing to PUSH - CASE_ONE");
        } else {
            List<FranchiseeEntity> dateFilter = franchiseeApplicantEntityList.stream().filter(this::isAfterTwoWeek).map(FranchiseeApplicantEntity::getFranchiseeEntity).collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_ONE);
        }
    }

    @Scheduled(cron = "0 1 16 * * *")
    private void caseFour() {
        TopicType topic = TopicType.REJECTED;
        List<FranchiseeApplicantEntity> franchiseeApplicantEntityList = franchiseeApplicantFindService.findByFranchiseeStatus(FranchiseeStatus.REJECTED);
        if (franchiseeApplicantEntityList.isEmpty()) {
            System.out.println("Nothing to PUSH - CASE_FOUR");
        } else {
            List<FranchiseeEntity> dateFilter = franchiseeApplicantEntityList.stream().filter(this::isAfterOneDay).map(FranchiseeApplicantEntity::getFranchiseeEntity).collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_FOUR);
        }
    }

    @Scheduled(cron = "0 1 * * * *")
    private void caseEight() {
        TopicType topic = TopicType.DISAPPEAR;
        List<PointEntity> pointEntityList = pointRepository.findByCreatedDateBefore(DisappearDate.DISAPPEAR_ALERT_DATE.getDisappearDate());
        if (pointEntityList.isEmpty()) {
            System.out.println("Nothing to PUSH - CASE_EIGHT");
        } else {
            List<FranchiseeEntity> dateFilter = pointEntityList.stream().map(PointEntity::getFranchiseeEntity).collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_EIGHT);
        }

    }

    private void pushNSave(TopicType topic, List<FranchiseeEntity> dateFilter, PushCategoryType pushCategoryType) {
        List<String> subscribeList = topicSubscribeService.subscribeByFranchisee(dateFilter, topic, SubscribeType.SUBSCRIBE);
        NotificationDto.Request request = new NotificationDto.Request(pushCategoryType, PushType.TOPIC, topic.toString());
        String send = pushNotificationService.sendMessageByTopic(request);
        topicSubscribeService.subscribeByFranchisee(dateFilter, topic, SubscribeType.UNSUBSCRIBE);
        subscribeList.stream().map(userPushTokenService::findByToken).forEach(entity -> pushHistorySaveService.saveHistory(request, send, entity));
    }

    private boolean isAfterOneDay(FranchiseeApplicantEntity franchiseeApplicantEntity) {
        LocalDateTime minusDays = LocalDateTime.now().minusDays(1);
        return franchiseeApplicantEntity.getCreatedDate().isBefore(minusDays);
    }

    private boolean isAfterTwoWeek(FranchiseeApplicantEntity franchiseeApplicantEntity) {
        LocalDateTime minusWeeks = LocalDateTime.now().minusWeeks(2);
        return franchiseeApplicantEntity.getCreatedDate().isBefore(minusWeeks);
    }
}
