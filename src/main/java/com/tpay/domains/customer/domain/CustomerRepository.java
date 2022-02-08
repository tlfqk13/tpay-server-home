package com.tpay.domains.customer.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
  Optional<CustomerEntity> findByCustomerNameAndPassportNumber(
      String customerName, String passportNumber);

  Optional<CustomerEntity> findByNationAndPassportNumber(
      String nationality, String passportNumber);
}
