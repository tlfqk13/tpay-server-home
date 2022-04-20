package com.tpay.domains.push.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.push.application.dto.UpdateIsReadDto;
import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PushHistoryService {

    private final PushHistoryRepository pushHistoryRepository;

    public List<PushHistoryEntity> findByFranchiseeIndex(Long franchiseeIndex) {
        return pushHistoryRepository.findByUserId(franchiseeIndex).get();
    }

    public PushHistoryEntity findByPushIndex(Long pushIndex) {
        return pushHistoryRepository.findById(pushIndex).get();
    }


    @Transactional
    public void updateIsRead(Long pushIndex, UpdateIsReadDto updateIsReadDto) {
        PushHistoryEntity pushHistoryEntity = pushHistoryRepository.findById(pushIndex).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid PushIndex"));
        pushHistoryEntity.updateIsRead(updateIsReadDto.getIsRead());
    }
}
