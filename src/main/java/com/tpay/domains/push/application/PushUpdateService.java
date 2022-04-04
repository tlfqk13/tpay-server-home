package com.tpay.domains.push.application;


import com.tpay.commons.push.detail.PushTopic;
import com.tpay.domains.push.domain.PushTokenEntity;
import com.tpay.domains.push.domain.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PushUpdateService {

    private final PushTokenRepository pushTokenRepository;


    @Transactional
    public void updateOrSaveByFranchiseeIndex(PushTokenEntity pushTokenEntity) {
        PushTokenEntity pushTokenEntity1 = pushTokenRepository.findByFranchiseeEntity(pushTokenEntity.getFranchiseeEntity())
            .orElseGet(() -> pushTokenRepository.save(pushTokenEntity));
        pushTokenEntity1.updateToken(pushTokenEntity, PushTopic.FRANCHISEE);
    }

    @Transactional
    public void updateOrSaveByEmployeeIndex(PushTokenEntity pushTokenEntity) {
        PushTokenEntity pushTokenEntity1 = pushTokenRepository.findByEmployeeEntity(pushTokenEntity.getEmployeeEntity())
            .orElseGet(() -> pushTokenRepository.save(pushTokenEntity));
        pushTokenEntity1.updateToken(pushTokenEntity, PushTopic.EMPLOYEE);
    }
}
