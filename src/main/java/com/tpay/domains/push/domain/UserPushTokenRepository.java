package com.tpay.domains.push.domain;

import com.tpay.commons.util.UserSelector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPushTokenRepository extends JpaRepository<UserPushTokenEntity, Long> {

    Optional<UserPushTokenEntity> findByUserIdAndUserSelector(Long userId, UserSelector userSelector);

    void deleteByUserIdAndUserSelector(Long userId, UserSelector userSelector);

    List<UserPushTokenEntity> findByUserSelector(UserSelector userSelector);

    Optional<UserPushTokenEntity> findByUserToken(String userToken);
}
