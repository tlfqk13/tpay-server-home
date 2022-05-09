package com.tpay.domains.push.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.push.application.dto.CountIsReadDto;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.application.dto.UpdateIsReadDto;
import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PushHistoryService {

    private final PushHistoryRepository pushHistoryRepository;

    public List<PushHistoryEntity> findByFranchiseeIndex(Long franchiseeIndex) {
        return pushHistoryRepository.findByUserIdOrderByIdDesc(franchiseeIndex).get();
    }

    public PushHistoryEntity findByPushIndex(Long pushIndex) {
        return pushHistoryRepository.findById(pushIndex).get();
    }


    @Transactional
    public void updateIsRead(Long pushIndex, UpdateIsReadDto updateIsReadDto) {
        PushHistoryEntity pushHistoryEntity = pushHistoryRepository.findById(pushIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid PushIndex"));
        pushHistoryEntity.updateIsRead(updateIsReadDto.getIsRead());
    }

    public CountIsReadDto countIsRead(Long franchiseeIndex) {
        long count = pushHistoryRepository.countByUserIdAndIsRead(franchiseeIndex, false);
        return CountIsReadDto.builder().count(count).build();
    }

    @Transactional
    public void saveHistory(NotificationDto.Request request, String send, UserPushTokenEntity userPushTokenEntity) {

        //요청 정보, 응답정보, 유저정보로 history SAVE
        PushHistoryEntity pushHistoryEntity = PushHistoryEntity.builder()
            .title(request.getTitle())
            .body(request.getBody())
            .pushCategory(request.getPushCategory())
            .link(request.getLink())
            .pushType(request.getPushType().toString())
            .pushTypeValue(userPushTokenEntity.getPushTokenEntity().getToken())
            .response(send)
            .userId(userPushTokenEntity.getFranchiseeEntity().getId())
            .isRead(false)
            .isDetail(true)
            .build();


        pushHistoryEntity.updateIsReadInit();
        pushHistoryEntity.updateIsDetail();
        pushHistoryRepository.save(pushHistoryEntity);
    }
}
