package com.tpay.domains.employee.domain;

import com.tpay.domains.employee.application.dto.EmployeeFindResponseInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
  Optional<EmployeeEntity> findByUserId(String userId);


  @Query(value = "select id as employeeIndex,name\n" +
      "from employee\n" +
      "where franchisee_id = :franchiseeIndex", nativeQuery = true)
  List<EmployeeFindResponseInterface> findAllByFranchiseeId(@Param("franchiseeIndex") Long franchiseeIndex);


  boolean existsByFranchiseeEntityIdAndUserId(Long franchiseeIndex, String userId);
}
