package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.FranchiseeInfo;
import com.tpay.domains.franchisee.application.dto.FranchiseeMyPageResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.order.application.OrderFindService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranchiseeFindService {
  private final FranchiseeRepository franchiseeRepository;
  private final OrderFindService orderFindService;
  private final FranchiseeApplicantFindService franchiseeApplicantFindService;

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
    FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByFranchiseeEntity(franchiseeEntity);

    List<OrderEntity> orderEntityList =
        orderFindService.findAllByFranchiseeEntityIndex(franchiseeIndex);

    Long totalSaleAmount =
        orderEntityList.stream()
            .filter(orderEntity -> orderEntity.getRefundEntity().getRefundStatus() != RefundStatus.CANCEL)
            .mapToLong(saleEntity -> Long.parseLong(saleEntity.getTotalAmount()))
            .sum();

    return FranchiseeMyPageResponse.builder()
        .createdDate(franchiseeEntity.getCreatedDate())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeName(franchiseeEntity.getStoreName())
        .storeAddressNumber(franchiseeEntity.getStoreAddressNumber())
        .storeAddressBasic(franchiseeEntity.getStoreAddressBasic())
        .storeAddressDetail(franchiseeEntity.getStoreAddressDetail())
        .totalSalesAmount(totalSaleAmount)
        .totalPoint(franchiseeEntity.getBalance())
        .productCategory(franchiseeEntity.getProductCategory())
        .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
        .sellerName(franchiseeEntity.getSellerName())
        .storeTel(franchiseeEntity.getStoreTel())
        .email(franchiseeEntity.getEmail())
        .signboard(franchiseeEntity.getSignboard())
        .storeNumber(franchiseeEntity.getStoreNumber())
        .build();
  }

  public List<FranchiseeInfo> findAll() {
    List<FranchiseeEntity> franchiseeEntityList = franchiseeRepository.findAll();

    return franchiseeEntityList.stream()
        .map(franchiseeEntity -> FranchiseeInfo.of(franchiseeEntity))
        .collect(Collectors.toList());
  }
}
