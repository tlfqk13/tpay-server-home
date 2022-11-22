package com.tpay.domains.auth.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseeAccessTokenRepository extends JpaRepository<FranchiseeAccessTokenEntity,Long> {
    boolean existsByFranchiseeEntityId(Long franchiseeIndex);

    Optional<FranchiseeAccessTokenEntity> findByFranchiseeEntityId(Long franchiseeIndex);

    void deleteByFranchiseeEntityId(Long franchiseeIndex);

    FranchiseeAccessTokenEntity findByFranchiseeEntity(FranchiseeEntity franchiseeEntity);

    boolean existsByFranchiseeEntityBusinessNumber(String businessNumber);

}
