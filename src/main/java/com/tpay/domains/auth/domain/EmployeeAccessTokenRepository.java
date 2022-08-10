package com.tpay.domains.auth.domain;

import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeAccessTokenRepository extends JpaRepository<EmployeeAccessTokenEntity,Long> {

    boolean existsByEmployeeEntityId(Long employeeIndex);

    Optional<EmployeeAccessTokenEntity> findByEmployeeEntityId(Long employeeIndex);

    void deleteByEmployeeEntityId(Long employeeIndex);

    EmployeeAccessTokenEntity findByEmployeeEntity(EmployeeEntity employeeEntity);

    Optional<EmployeeAccessTokenEntity> findByEmployeeEntity_UserId(String userId);

}
