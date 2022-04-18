package com.tpay.domains.push.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import com.tpay.domains.push.domain.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tpay.commons.util.UserSelector.*;


@Service
@RequiredArgsConstructor
public class UserPushTokenService {

    private final UserPushTokenRepository userPushTokenRepository;

    @Transactional
    public void save(UserPushTokenEntity userPushTokenEntity) {

        Optional<UserPushTokenEntity> optionalUserPushTokenEntity = userPushTokenRepository.findByUserIdAndUserSelector(userPushTokenEntity.getUserId(), userPushTokenEntity.getUserSelector());
        if (optionalUserPushTokenEntity.isEmpty()) {
            userPushTokenRepository.save(userPushTokenEntity);
        } else {
            optionalUserPushTokenEntity.get().updateToken(userPushTokenEntity.getUserToken());

        }
    }

    @Transactional
    public Optional<UserPushTokenEntity> findByUserIdAndUserType(Long userId, UserSelector userType) {
        return userPushTokenRepository.findByUserIdAndUserSelector(userId, userType);
    }

    @Transactional
    public UserPushTokenEntity findByFranchiseeIndex(Long franchiseeIndex) {
        return userPushTokenRepository.findByUserIdAndUserSelector(franchiseeIndex, FRANCHISEE)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "findByFranchiseeIndex Error"));
    }

    public List<String> findToken(String topic) {
        List<String> tokenList = new ArrayList<>();
        if (topic.equals("FRANCHISEE")) {
            List<UserPushTokenEntity> franchisee = userPushTokenRepository.findByUserSelector(FRANCHISEE);
            franchisee.forEach(entity -> tokenList.add(entity.getUserToken()));
        } else if (topic.equals("ALL")) {
            List<UserPushTokenEntity> all = userPushTokenRepository.findAll();
            all.forEach(entity -> tokenList.add(entity.getUserToken()));
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "topic must FRANCHISEE or ALL");
        }
        return tokenList;

    }

    public UserPushTokenEntity findByToken(String token) {
        Optional<UserPushTokenEntity> byUserToken = userPushTokenRepository.findByUserToken(token);
        if (byUserToken.isEmpty()) {
            System.out.println("User Not exists in token table. token : " + token);
        }

        return byUserToken.get();
    }
}
