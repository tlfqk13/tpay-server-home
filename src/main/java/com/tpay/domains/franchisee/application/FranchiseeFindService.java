package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.FranchiseeMyPageResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.order.application.OrderFindService;
import com.tpay.domains.order.domain.OrderEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeFindService {
  private final FranchiseeRepository franchiseeRepository;
  private final OrderFindService orderFindService;

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
        orderFindService.findAllByFranchiseeEntityIndex(franchiseeIndex);

    Long totalSaleAmount =
        orderEntityList.stream()
            .mapToLong(saleEntity -> Long.parseLong(saleEntity.getTotalAmount()))
            .sum();

    return FranchiseeMyPageResponse.builder()
        .createdDate(franchiseeEntity.getCreatedDate())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeName(franchiseeEntity.getStoreName())
        .storeAddress(franchiseeEntity.getStoreAddress())
        .totalSalesAmount(totalSaleAmount)
        .totalPoint(franchiseeEntity.getBalance())
        .productCategory(franchiseeEntity.getProductCategory())
        .build();
  }
}
