package com.tpay.domains.push.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.push.domain.PushTokenEntity;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserPushTokenService {

    private final UserPushTokenRepository userPushTokenRepository;
    private final PushTokenService pushTokenService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    public UserPushTokenEntity deleteIfExistsAndSave(FranchiseeEntity franchiseeEntity, PushTokenEntity pushTokenEntity) {
        if (userPushTokenRepository.existsByFranchiseeEntity(franchiseeEntity)) {
            userPushTokenRepository.deleteByFranchiseeEntity(franchiseeEntity);
            logger.trace("[franchiseeIndex : {}] franchisee already Exists, Deleted", franchiseeEntity.getId());
        }
        if (userPushTokenRepository.existsByPushTokenEntity(pushTokenEntity)) {
            userPushTokenRepository.deleteByPushTokenEntity(pushTokenEntity);
            logger.trace("[token : {}] token already exists, Deleted", pushTokenEntity.getToken());
        }
        UserPushTokenEntity userPushTokenEntity = UserPushTokenEntity.builder()
            .franchiseeEntity(franchiseeEntity)
            .pushTokenEntity(pushTokenEntity)
            .build();
        logger.trace("[franchiseeIndex : {}],[tokenIndex : {}] Saved", franchiseeEntity.getId(), pushTokenEntity.getId());
        return userPushTokenRepository.save(userPushTokenEntity);
    }

    @Transactional
    public Optional<UserPushTokenEntity> optionalFindByFranchiseeIndex(Long franchiseeIndex) {
        return userPushTokenRepository.findByFranchiseeEntityId(franchiseeIndex);
    }

    public List<String> findAllToken() {
        List<String> tokenList = new ArrayList<>();
        List<UserPushTokenEntity> franchisee = userPushTokenRepository.findAll();
        franchisee.forEach(entity -> tokenList.add(entity.getPushTokenEntity().getToken()));
        return tokenList;

    }

    public UserPushTokenEntity findByToken(String token) {
        PushTokenEntity pushTokenEntity = pushTokenService.findByToken(token).orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Token Not Exists"));
        Optional<UserPushTokenEntity> byUserToken = userPushTokenRepository.findByPushTokenEntity(pushTokenEntity);
        if (byUserToken.isEmpty()) {
            System.out.println("User Not exists in token table. token : " + token);
        }
        return byUserToken.get();
    }
}
