package com.tpay.domains.mypage.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.mypage.application.dto.MyPageResponse;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final FranchiseeRepository franchiseeRepository;
  private final SaleRepository saleRepository;

  public ResponseEntity<MyPageResponse> mypage(Long franchiseeId) {

    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findById(franchiseeId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee ID"));
    List<SaleEntity> saleEntityList = saleRepository.findAllByFranchiseeEntityId(franchiseeId);

    Long totalSaleAmount =
        saleEntityList.stream()
            .mapToLong(saleEntity -> Long.parseLong(saleEntity.getTotalAmount()))
            .sum();

    return ResponseEntity.ok(
        MyPageResponse.builder()
            .storeName(franchiseeEntity.getStoreName())
            .createdDate(franchiseeEntity.getCreatedDate())
            .totalPoint(franchiseeEntity.getBalance())
            .totalSalesAmount(totalSaleAmount)
            .build());
  }
}
