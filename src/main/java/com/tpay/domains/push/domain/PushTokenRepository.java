package com.tpay.domains.push.domain;

import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PushTokenRepository extends JpaRepository<PushTokenEntity, Long> {

    void deleteByFranchiseeEntityId(Long franchiseeIndex);
    void deleteByEmployeeEntityId(Long employeeIndex);

    Optional<PushTokenEntity> findByFranchiseeEntity(FranchiseeEntity franchiseeEntity);
    Optional<PushTokenEntity> findByEmployeeEntity(EmployeeEntity employeeEntity);
}
