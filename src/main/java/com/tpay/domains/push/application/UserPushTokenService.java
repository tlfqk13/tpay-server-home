package com.tpay.domains.push.application;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserPushTokenService {

    private final UserPushTokenRepository userPushTokenRepository;

    @Transactional
    public void save(UserPushTokenEntity userPushTokenEntity) {

        Optional<UserPushTokenEntity> optionalUserPushTokenEntity = userPushTokenRepository.findByUserIdAndUserType(userPushTokenEntity.getUserId(), userPushTokenEntity.getUserType());
        if(optionalUserPushTokenEntity.isEmpty()){
            userPushTokenRepository.save(userPushTokenEntity);
        }
        else {
            optionalUserPushTokenEntity.get().updateToken(userPushTokenEntity.getUserToken());

        }
    }

    @Transactional
    public Optional<UserPushTokenEntity> findByUserIdAndUserType(String userId, UserSelector userType) {
        return userPushTokenRepository.findByUserIdAndUserType(userId, userType);
    }

    @Transactional
    public String findTokenByUserIdAndUserType(String userId, UserSelector userType) {
        return findByUserIdAndUserType(userId,userType).orElseThrow().getUserToken();
    }

}
