package com.tpay.domains.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeAccessTokenRepository extends JpaRepository<EmployeeAccessTokenEntity,Long> {

    boolean existsByEmployeeEntityId(Long employeeIndex);

    Optional<EmployeeAccessTokenEntity> findByEmployeeEntityId(Long employeeIndex);

    void deleteByEmployeeEntityId(Long employeeIndex);

    boolean existsByEmployeeEntityUserId(String userId);

}
