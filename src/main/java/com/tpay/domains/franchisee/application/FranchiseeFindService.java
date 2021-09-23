package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.FranchiseeMyPageResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.order.application.SaleFindService;
import com.tpay.domains.order.domain.OrderEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeFindService {
  private final FranchiseeRepository franchiseeRepository;
  private final SaleFindService saleFindService;

  public FranchiseeEntity findByBusinessNumber(String businessNumber) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findByBusinessNumber(businessNumber.replaceAll("-", ""))
            .orElseThrow(
                () ->
                    new InvalidParameterException(
                        ExceptionState.INVALID_PARAMETER, "Invalid Business Number"));

    return franchiseeEntity;
  }

  public FranchiseeEntity findByIndex(Long franchiseeIndex) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findById(franchiseeIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Index"));

    return franchiseeEntity;
  }

  public FranchiseeMyPageResponse findMyPageInfo(Long franchiseeIndex) {
    FranchiseeEntity franchiseeEntity = this.findByIndex(franchiseeIndex);

    List<OrderEntity> orderEntityList =
        saleFindService.findAllByFranchiseeEntityIndex(franchiseeIndex);

    Long totalSaleAmount =
        orderEntityList.stream()
            .mapToLong(saleEntity -> Long.parseLong(saleEntity.getTotalAmount()))
            .sum();

    return FranchiseeMyPageResponse.builder()
        .storeName(franchiseeEntity.getStoreName())
        .createdDate(franchiseeEntity.getCreatedDate())
        .totalPoint(franchiseeEntity.getBalance())
        .totalSalesAmount(totalSaleAmount)
        .build();
  }
}
