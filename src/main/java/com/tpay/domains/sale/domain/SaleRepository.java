package com.tpay.domains.sale.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
  List<SaleEntity> findAllByFranchiseeEntityId(Long franchiseeId);

  List<SaleEntity> findAllByCustomerEntityId(Long customerIndex);

//  List<SaleEntity> findAllByFranchiseeEntityIdAndCreatedDateBetween(Long franchiseeIndex, LocalDateTime startDate,LocalDateTime endDate , Pageable pageable);
  List<SaleEntity> findAllByFranchiseeEntityIdAndCreatedDateBetween(Long franchiseeIndex, LocalDateTime startDate, LocalDateTime endDate);


  List<SaleEntity> findByFranchiseeEntityAndSaleDateContaining(FranchiseeEntity franchiseeEntity, String saleDate);

  SaleEntity findByOrderNumber(String orderNumber);

}
