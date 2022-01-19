package com.tpay.domains.franchisee_upload.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseeBankRepository extends JpaRepository<FranchiseeBankEntity, Long> {
  boolean existsByFranchiseeEntity(FranchiseeEntity franchiseeEntity);
}
