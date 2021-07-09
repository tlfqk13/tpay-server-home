package com.tpay.domains.mypage.application;

import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.mypage.application.dto.MyPageResponse;
import com.tpay.domains.sale.application.SaleFindService;
import com.tpay.domains.sale.domain.SaleEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final FranchiseeFindService franchiseeFindService;
  private final SaleFindService saleFindService;

  public MyPageResponse mypage(Long franchiseeIndex) {

    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    List<SaleEntity> saleEntityList =
        saleFindService.findAllByFranchiseeEntityIndex(franchiseeIndex);

    Long totalSaleAmount =
        saleEntityList.stream()
            .mapToLong(saleEntity -> Long.parseLong(saleEntity.getTotalAmount()))
            .sum();

    return MyPageResponse.builder()
        .storeName(franchiseeEntity.getStoreName())
        .createdDate(franchiseeEntity.getCreatedDate())
        .totalPoint(franchiseeEntity.getBalance())
        .totalSalesAmount(totalSaleAmount)
        .build();
  }
}
