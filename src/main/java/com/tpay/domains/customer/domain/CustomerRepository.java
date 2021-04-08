package com.tpay.domains.customer.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  Optional<CustomerEntity> findByCustomerNameAndPassportNumber(
      String customerName, String passportNumber);
}
