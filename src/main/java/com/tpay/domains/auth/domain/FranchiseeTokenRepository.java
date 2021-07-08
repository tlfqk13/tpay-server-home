package com.tpay.domains.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseeTokenRepository extends JpaRepository<FranchiseeTokenEntity, Long> {
  boolean existsByFranchiseeEntityId(Long franchiseeIndex);

  Optional<FranchiseeTokenEntity> findByFranchiseeEntityId(Long franchiseeIndex);

  void deleteByFranchiseeEntityId(Long franchiseeIndex);
}
