package com.tpay.domains.push.application;

import com.tpay.domains.push.domain.PushHistoryEntity;
import com.tpay.domains.push.domain.PushHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushHistoryService {

    private final PushHistoryRepository pushHistoryRepository;

    public List<PushHistoryEntity> findByFranchiseeIndex(Long franchiseeIndex) {
        return pushHistoryRepository.findByUserId(franchiseeIndex).get();
    }
}
