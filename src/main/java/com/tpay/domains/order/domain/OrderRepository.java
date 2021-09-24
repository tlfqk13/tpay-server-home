package com.tpay.domains.order.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  Optional<List<OrderEntity>> findAllByFranchiseeEntityId(Long franchiseeId);

  List<OrderEntity> findAllByCustomerEntityId(Long customerIndex);

//  List<SaleEntity> findAllByFranchiseeEntityIdAndCreatedDateBetween(Long franchiseeIndex, LocalDateTime startDate,LocalDateTime endDate , Pageable pageable);
  List<OrderEntity> findAllByFranchiseeEntityIdAndCreatedDateBetween(Long franchiseeIndex, LocalDateTime startDate, LocalDateTime endDate);


  List<OrderEntity> findByFranchiseeEntityAndSaleDateContaining(FranchiseeEntity franchiseeEntity, String saleDate);

  OrderEntity findByOrderNumber(String orderNumber);

}
