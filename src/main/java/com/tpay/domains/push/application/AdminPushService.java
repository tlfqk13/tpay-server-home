package com.tpay.domains.push.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.domains.notice.application.NoticeService;
import com.tpay.domains.push.application.dto.*;
import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import com.tpay.domains.push.domain.SubscribeType;
import com.tpay.domains.push.domain.TopicType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminPushService {

    private final TopicSubscribeService topicSubscribeService;
    private final PushNotificationService pushNotificationService;
    private final PushHistoryService pushHistoryService;

    private final UserPushTokenService userPushTokenService;
    private final PushHistoryRepository pushHistoryRepository;

    private final NoticeService noticeService;

    @Transactional
    public AdminNotificationDto.Response sendMessageByAdmin(AdminNotificationDto.Request adminRequest) {
        TopicType topic = TopicType.ALL;
        List<String> subscribeList = topicSubscribeService.subscribeByTopic(topic, SubscribeType.SUBSCRIBE);
        if (!subscribeList.isEmpty()) {
            NotificationDto.Request request = new NotificationDto.Request(PushCategoryType.CASE_FIFTEEN, PushType.TOPIC, topic.toString(), adminRequest.getTitle(), adminRequest.getBody());
            String send = pushNotificationService.sendMessageByTopic(request);
            topicSubscribeService.subscribeByTopic(topic, SubscribeType.UNSUBSCRIBE);
            subscribeList.stream().map(userPushTokenService::findByToken).map(entity -> pushHistoryService.saveHistory(request, send, entity.get())).forEach(entity -> entity.updateNoticeIndex(adminRequest.getNoticeIndex()));
            return AdminNotificationDto.Response.builder()
                .message(send)
                .build();
        } else {
            return AdminNotificationDto.Response.builder()
                .message("No Records in user_push_token table")
                .build();
        }


    }

    public PushFindDto.FindAllResponse findAll() {
        List<AdminPushDto> allAnnouncement = pushHistoryRepository.findAllAnnouncement();
        List<AdminPushResponse> collect = allAnnouncement.stream().map(AdminPushResponse::new).collect(Collectors.toList());
        return PushFindDto.FindAllResponse.builder()
            .responseList(collect)
            .build();
    }

    public PushFindDto.Response findDetail(Long pushIndex) {
        PushHistoryEntity pushHistoryEntity = pushHistoryRepository.findById(pushIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid PushIndex"));
        return PushFindDto.Response.builder()
            .title(pushHistoryEntity.getTitle())
            .body(pushHistoryEntity.getBody())
            .noticeIndex(pushHistoryEntity.getNoticeIndex())
            .build();

    }

    public PushNoticeDto findAllNotice() {
        return noticeService.findAllNotice();
    }
}
