package com.tpay.domains.employee.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByUserId(String userId);

    List<EmployeeEntity> findByFranchiseeEntityIdAndIsDelete(Long franchiseeIndex, Boolean isDelete);

    boolean existsByUserId(String userId);
}
