package com.tpay.domains.customer.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>, CustomerRepositoryCustom {
    Optional<CustomerEntity> findByNationAndPassportNumber(
        String nation, String passportNumber);

    boolean existsByPassportNumber(String passportNumber);

    Optional<CustomerEntity> findByPassportNumber(String passportNumber);
}
