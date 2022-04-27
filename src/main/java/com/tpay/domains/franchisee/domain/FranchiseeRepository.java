package com.tpay.domains.franchisee.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranchiseeRepository extends JpaRepository<FranchiseeEntity, Long> {
    boolean existsByBusinessNumber(String businessNumber);

    Optional<FranchiseeEntity> findByBusinessNumber(String businessNumber);

    Optional<FranchiseeEntity> findBySellerNameAndStoreTel(String sellerName, String storeTel);

}
