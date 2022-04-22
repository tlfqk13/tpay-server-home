package com.tpay.domains.push.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.push.PushType;
import com.tpay.domains.push.application.dto.AdminNotificationDto;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.application.dto.PushFindDto;
import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import com.tpay.domains.push.domain.SubscribeType;
import com.tpay.domains.push.domain.TopicType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPushService {

    private final TopicSubscribeService topicSubscribeService;
    private final PushNotificationService pushNotificationService;
    private final PushHistoryService pushHistoryService;

    private final UserPushTokenService userPushTokenService;
    private final PushHistoryRepository pushHistoryRepository;

    @Transactional
    public AdminNotificationDto.Response sendMessageByAdmin(AdminNotificationDto.Request adminRequest) {
        TopicType topic = TopicType.ALL;
        List<String> subscribeList = topicSubscribeService.subscribeByTopic(topic, SubscribeType.SUBSCRIBE);
        NotificationDto.Request request = new NotificationDto.Request(PushCategoryType.CASE_FIFTEEN, PushType.TOPIC, topic.toString(), adminRequest.getTitle(), adminRequest.getBody());
        String send = pushNotificationService.sendMessageByTopic(request);
        topicSubscribeService.subscribeByTopic(topic, SubscribeType.UNSUBSCRIBE);
        subscribeList.stream().map(userPushTokenService::findByToken).forEach(entity -> pushHistoryService.saveHistory(request, send, entity));
        return AdminNotificationDto.Response.builder()
            .message(send)
            .build();
    }

    public PushFindDto.FindAllResponse findAll() {
        List<PushHistoryEntity> pushHistoryEntityList = pushHistoryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<PushFindDto.Response> responseList = new ArrayList<>();
        for (PushHistoryEntity pushHistoryEntity : pushHistoryEntityList) {
            PushFindDto.Response response = PushFindDto.Response.builder()
                .pushIndex(pushHistoryEntity.getId())
                .title(pushHistoryEntity.getTitle())
                .createdDate(pushHistoryEntity.getCreatedDate())
                .build();
            responseList.add(response);
        }
        return PushFindDto.FindAllResponse.builder()
            .responseList(responseList)
            .build();
    }

    public PushFindDto.Response findDetail(Long pushIndex) {
        PushHistoryEntity pushHistoryEntity = pushHistoryRepository.findById(pushIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid PushIndex"));
        return PushFindDto.Response.builder()
            .title(pushHistoryEntity.getTitle())
            .body(pushHistoryEntity.getBody())
            .build();

    }
}
