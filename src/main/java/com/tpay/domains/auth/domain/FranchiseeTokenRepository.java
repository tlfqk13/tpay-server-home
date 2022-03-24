package com.tpay.domains.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseeTokenRepository extends JpaRepository<FranchiseeTokenEntity, Long> {
    boolean existsByFranchiseeEntityId(Long franchiseeIndex);

    Optional<FranchiseeTokenEntity> findByFranchiseeEntityId(Long franchiseeIndex);

    void deleteByFranchiseeEntityId(Long franchiseeIndex);
}
