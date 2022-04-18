package com.tpay.domains.push.domain;

import com.tpay.commons.util.UserSelector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPushTokenRepository extends JpaRepository<UserPushTokenEntity, Long> {

    Optional<UserPushTokenEntity> findByUserIdAndUserType(Long userId, UserSelector userType);

    void deleteByUserIdAndUserType(String userId, UserSelector userType);


    List<UserPushTokenEntity> findByUserType(String userType);

    Optional<UserPushTokenEntity> findByUserToken(String userToken);
}
