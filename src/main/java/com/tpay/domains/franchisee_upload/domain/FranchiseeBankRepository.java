package com.tpay.domains.franchisee_upload.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseeBankRepository extends JpaRepository<FranchiseeBankEntity, Long> {
    boolean existsByFranchiseeEntity(FranchiseeEntity franchiseeEntity);

    Optional<FranchiseeBankEntity> findByFranchiseeEntity(FranchiseeEntity franchiseeEntity);

    void deleteByFranchiseeEntityId(Long franchiseeIndex);
}
