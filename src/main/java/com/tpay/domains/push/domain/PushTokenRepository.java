package com.tpay.domains.push.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PushTokenRepository extends JpaRepository<PushTokenEntity,Long> {

    boolean existsByToken(String token);

    Optional<PushTokenEntity> findByToken(String token);
}
