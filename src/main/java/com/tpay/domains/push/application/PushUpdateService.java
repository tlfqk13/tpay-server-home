package com.tpay.domains.push.application;


import com.tpay.domains.push.domain.PushTokenEntity;
import com.tpay.domains.push.domain.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushUpdateService {

    private final PushTokenRepository pushTokenRepository;


    public void updateOrSaveByFranchiseeIndex(PushTokenEntity pushTokenEntity) {
        PushTokenEntity pushTokenEntity1 = pushTokenRepository.findByFranchiseeEntity(pushTokenEntity.getFranchiseeEntity())
            .orElseGet(() -> pushTokenRepository.save(pushTokenEntity));
        pushTokenEntity1.updateToken(pushTokenEntity);
    }

    public void updateOrSaveByEmployeeIndex(PushTokenEntity pushTokenEntity) {
        PushTokenEntity pushTokenEntity1 = pushTokenRepository.findByEmployeeEntity(pushTokenEntity.getEmployeeEntity())
            .orElseGet(() -> pushTokenRepository.save(pushTokenEntity));
        pushTokenEntity1.updateToken(pushTokenEntity);
    }
}
