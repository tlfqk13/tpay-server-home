package com.tpay.domains.push.application;

import com.tpay.domains.push.domain.PushTokenEntity;
import com.tpay.domains.push.domain.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PushTokenService {

    private final PushTokenRepository pushTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public PushTokenEntity saveIfNotExists(PushTokenEntity pushTokenEntity){
        if(!pushTokenRepository.existsByToken(pushTokenEntity.getToken())){
            logger.trace("[token : {}] saved", pushTokenEntity.getToken());
            return pushTokenRepository.save(pushTokenEntity);
        } else {
            logger.trace("[token : {}] Found", pushTokenEntity.getToken());
            return pushTokenRepository.findByToken(pushTokenEntity.getToken()).get();
        }
    }

    public Optional<PushTokenEntity> findByToken(String token) {
        return pushTokenRepository.findByToken(token);
    }
}
