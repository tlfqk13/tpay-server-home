package com.tpay.domains.push.application;

import com.tpay.domains.push.domain.PushTokenEntity;
import com.tpay.domains.push.domain.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PushTokenService {

    private final PushTokenRepository pushTokenRepository;

    @Transactional
    public PushTokenEntity saveIfNotExists(PushTokenEntity pushTokenEntity) {
        if (!pushTokenRepository.existsByToken(pushTokenEntity.getToken())) {
            log.trace("[token : {}] saved", pushTokenEntity.getToken());
            return pushTokenRepository.save(pushTokenEntity);
        } else {
            log.trace("[token : {}] Found", pushTokenEntity.getToken());
            return pushTokenRepository.findByToken(pushTokenEntity.getToken()).get();
        }
    }

    public Optional<PushTokenEntity> findByToken(String token) {
        return pushTokenRepository.findByToken(token);
    }
}
