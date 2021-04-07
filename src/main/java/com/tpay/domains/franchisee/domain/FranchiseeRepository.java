package com.tpay.domains.franchisee.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseeRepository extends JpaRepository<FranchiseeEntity, Long> {
  boolean existsByBusinessNumber(String businessNumber);
}
