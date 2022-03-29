package com.tpay.domains.push.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PushTokenRepository extends JpaRepository<PushTokenEntity, Long> {

    void deleteByFranchiseeEntityId(Long franchiseeIndex);
    void deleteByEmployeeEntityId(Long employeeIndex);
}
