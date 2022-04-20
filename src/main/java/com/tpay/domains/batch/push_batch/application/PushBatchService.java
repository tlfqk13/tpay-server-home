package com.tpay.domains.batch.push_batch.application;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.commons.util.DisappearDate;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.push.application.PushHistorySaveService;
import com.tpay.domains.push.application.PushNotificationService;
import com.tpay.domains.push.application.TopicSubscribeService;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.SubscribeType;
import com.tpay.domains.push.domain.TopicType;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.tpay.commons.push.PushCategoryType.*;

@Service
@RequiredArgsConstructor
public class PushBatchService {

    //초기화시 자동으로 한 번 메서드가 실행됨. 방지용 검증
    private static final String firstCallTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final PushNotificationService pushNotificationService;
    private final TopicSubscribeService topicSubscribeService;
    private final UserPushTokenService userPushTokenService;
    private final PushHistorySaveService pushHistorySaveService;
    private final PointRepository pointRepository;
    private final RefundRepository refundRepository;

    public void batchPush() {
        caseOne();
        caseFour();
        caseEight();
        caseNine();
        caseTen();
        caseEleven();
    }


    @Scheduled(cron = "0 0 14 * * *")
    public void caseOne() {
        TopicType topic = TopicType.INIT;
        List<FranchiseeApplicantEntity> franchiseeApplicantEntityList = franchiseeApplicantFindService.findByFranchiseeStatus(FranchiseeStatus.INIT);
        if (isFirstCall()) {
            System.out.println("First Call");
        } else if (franchiseeApplicantEntityList.isEmpty()) {
            System.out.println("[" + LocalDateTime.now() + "] Nothing to PUSH - CASE_ONE");
        } else {
            List<FranchiseeEntity> dateFilter = franchiseeApplicantEntityList.stream().filter(this::isAfterTwoWeek).map(FranchiseeApplicantEntity::getFranchiseeEntity).collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_ONE);
        }
    }

    @Scheduled(cron = "0 1 16 * * *")
    private void caseFour() {
        TopicType topic = TopicType.REJECTED;
        List<FranchiseeApplicantEntity> franchiseeApplicantEntityList = franchiseeApplicantFindService.findByFranchiseeStatus(FranchiseeStatus.REJECTED);

        if (isFirstCall()) {
            System.out.println("First Call");
        } else if (franchiseeApplicantEntityList.isEmpty()) {
            System.out.println("[" + LocalDateTime.now() + "]Nothing to PUSH - CASE_FOUR");
        } else {
            List<FranchiseeEntity> dateFilter = franchiseeApplicantEntityList.stream().filter(this::isAfterOneDay).map(FranchiseeApplicantEntity::getFranchiseeEntity).collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_FOUR);
        }
    }

    @Scheduled(cron = "0 1 15 * * *")
    private void caseEight() {
        TopicType topic = TopicType.DISAPPEAR;
        List<PointEntity> pointEntityList = pointRepository.findByCreatedDateBefore(DisappearDate.DISAPPEAR_ALERT_DATE.getDisappearDate());
        if (isFirstCall()) {
            System.out.println("First Call");
        } else if (pointEntityList.isEmpty()) {
            System.out.println("[" + LocalDateTime.now() + "]Nothing to PUSH - CASE_EIGHT");
        } else {
            List<FranchiseeEntity> dateFilter = pointEntityList.stream().map(PointEntity::getFranchiseeEntity).collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_EIGHT);
        }
    }

    @Scheduled(cron = "0 0 20 L * ?")
    private void caseNine() {
        TopicType topic = TopicType.STAT_MONTH;
        LocalDateTime startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        List<RefundEntity> refundEntityList = refundRepository.findByCreatedDateBetween(startDate, endDate);
        if (isFirstCall()) {
            System.out.println("First Call");
        } else if (refundEntityList.isEmpty()) {
            System.out.println("[" + LocalDateTime.now() + "]Nothing to PUSH - CASE_NINE");
        } else {
            List<FranchiseeEntity> dateFilter = refundEntityList.stream().map(RefundEntity::getOrderEntity).map(OrderEntity::getFranchiseeEntity).distinct().collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_NINE);
        }
    }

    @Scheduled(cron = "0 10 20 31 12 *")
    private void caseTen() {
        TopicType topic = TopicType.STAT_YEAR;
        LocalDateTime startDate = LocalDate.now().minusYears(1).withDayOfYear(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().withDayOfYear(1).atStartOfDay();
        List<RefundEntity> refundEntityList = refundRepository.findByCreatedDateBetween(startDate, endDate);
        if (isFirstCall()) {
            System.out.println("First Call");
        } else if (refundEntityList.isEmpty()) {
            System.out.println("[" + LocalDateTime.now() + "]Nothing to PUSH - CASE_TEN");
        } else {
            List<FranchiseeEntity> dateFilter = refundEntityList.stream().map(RefundEntity::getOrderEntity).map(OrderEntity::getFranchiseeEntity).distinct().collect(Collectors.toList());
            pushNSave(topic, dateFilter, CASE_TEN);
        }
    }

    @Scheduled(cron = "0 0 13 1 7,1 *")
    private void caseEleven() {
        TopicType topic = TopicType.VAT;
        LocalDateTime startDate = LocalDate.now().minusMonths(6).withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        List<RefundEntity> refundEntityList = refundRepository.findByCreatedDateBetween(startDate, endDate);
        Month nowMonth = LocalDate.now().getMonth();
        if (isFirstCall()) {
            System.out.println("First Call");
        } else if(!(nowMonth.equals(Month.JANUARY)||nowMonth.equals(Month.JULY))) {
            System.out.println(nowMonth +" is NOT JAN OR JULY");
        } else if (refundEntityList.isEmpty()) {
            System.out.println("[" + LocalDateTime.now() + "]Nothing to PUSH - CASE_ELEVEN");
        } else {
            List<FranchiseeEntity> dateFilter = refundEntityList.stream().map(RefundEntity::getOrderEntity).map(OrderEntity::getFranchiseeEntity).distinct().collect(Collectors.toList());
            pushNSave(topic,dateFilter,CASE_ELEVEN);
        }
    }

    // TODO: 2022/04/19 CASE 12랑 13 만들어야함


    private void pushNSave(TopicType topic, List<FranchiseeEntity> dateFilter, PushCategoryType pushCategoryType) {
        List<String> subscribeList = topicSubscribeService.subscribeByFranchisee(dateFilter, topic, SubscribeType.SUBSCRIBE);
        NotificationDto.Request request = new NotificationDto.Request(pushCategoryType, PushType.TOPIC, topic.toString());
        String send = pushNotificationService.sendMessageByTopic(request);
        topicSubscribeService.subscribeByFranchisee(dateFilter, topic, SubscribeType.UNSUBSCRIBE);
        subscribeList.stream().map(userPushTokenService::findByToken).forEach(entity -> pushHistorySaveService.saveHistory(request, send, entity));
        System.out.println("[" + LocalDateTime.now() + "] Pushed - " + pushCategoryType);
    }

    private boolean isAfterOneDay(FranchiseeApplicantEntity franchiseeApplicantEntity) {
        LocalDateTime minusDays = LocalDateTime.now().minusDays(1);
        return franchiseeApplicantEntity.getCreatedDate().isBefore(minusDays);
    }

    private boolean isAfterTwoWeek(FranchiseeApplicantEntity franchiseeApplicantEntity) {
        LocalDateTime minusWeeks = LocalDateTime.now().minusWeeks(2);
        return franchiseeApplicantEntity.getCreatedDate().isBefore(minusWeeks);
    }

    private static boolean isFirstCall() {
        return (firstCallTime.equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))) || firstCallTime.equals(LocalDateTime.now().minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))));
    }
}
