package com.tpay.domains.push.domain;

import com.tpay.commons.util.UserSelector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPushTokenRepository extends JpaRepository<UserPushTokenEntity, Long> {

    Optional<UserPushTokenEntity> findByUserIdAndUserType(String userId, UserSelector userType);

}
