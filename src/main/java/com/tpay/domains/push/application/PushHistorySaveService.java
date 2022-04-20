package com.tpay.domains.push.application;

import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PushHistorySaveService {

    private final PushHistoryRepository pushHistoryRepository;


    @Transactional
    public void saveHistory(NotificationDto.Request request, String send, UserPushTokenEntity userPushTokenEntity) {
        //요청 정보, 응답정보, 유저정보로 history SAVE
        PushHistoryEntity pushHistoryEntity = PushHistoryEntity.builder()
            .title(request.getTitle())
            .body(request.getBody())
            .pushCategory(request.getPushCategory())
            .link(request.getLink())
            .pushType(request.getPushType().toString())
            .pushTypeValue(userPushTokenEntity.getUserToken())
            .response(send)
            .userSelector(userPushTokenEntity.getUserSelector())
            .userId(userPushTokenEntity.getUserId())
            .isRead(false)
            .isDetail(false)
            .build();
        pushHistoryEntity.updateIsDetail();
        pushHistoryRepository.save(pushHistoryEntity);
    }
}