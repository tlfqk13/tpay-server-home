package com.tpay.domains.order.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderFindService {
  private final OrderRepository orderRepository;

  @Transactional
  public List<OrderEntity> findAllByFranchiseeEntityIndex(Long franchiseeIndex) {
    return orderRepository
        .findAllByFranchiseeEntityId(franchiseeIndex)
        .orElse(Collections.emptyList());
  }

  @Transactional
  public Long sumTotalSaleAmountByFranchiseeIndex(Long franchiseeIndex){
    Optional<Long> optionalResult = orderRepository.sumTotalSaleAmountByFranchiseeIndex(franchiseeIndex);
    if(optionalResult.isEmpty()){
      return 0L;
    }
    else return optionalResult.get();
  }
}
