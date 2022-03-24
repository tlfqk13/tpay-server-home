package com.tpay.domains.auth.domain;

import com.tpay.domains.employee.domain.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeTokenRepository extends JpaRepository<EmployeeTokenEntity, Long> {

    boolean existsByEmployeeEntity(EmployeeEntity employeeEntity);

    Optional<EmployeeTokenEntity> findByEmployeeEntity(EmployeeEntity employeeEntity);

    void deleteByEmployeeEntity_Id(Long employeeIndex);
}
