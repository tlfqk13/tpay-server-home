package com.tpay.domains.employee.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
  Optional<EmployeeEntity> findByUserId(String userId);
}
