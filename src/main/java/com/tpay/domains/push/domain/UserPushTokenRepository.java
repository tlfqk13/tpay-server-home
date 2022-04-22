package com.tpay.domains.push.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPushTokenRepository extends JpaRepository<UserPushTokenEntity, Long> {

    Optional<UserPushTokenEntity> findByFranchiseeEntityId(Long userId);

    Optional<UserPushTokenEntity> findByPushTokenEntity(PushTokenEntity pushTokenEntity);

    boolean existsByFranchiseeEntity(FranchiseeEntity franchiseeEntity);

    void deleteByFranchiseeEntity(FranchiseeEntity franchiseeEntity);

    boolean existsByPushTokenEntity(PushTokenEntity pushTokenEntity);

    void deleteByPushTokenEntity(PushTokenEntity pushTokenEntity);
}
