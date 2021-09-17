package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.BusinessNumberFindRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessNumberFindService {

  private final FranchiseeRepository franchiseeRepository;

  @Transactional
  public String findBusinessNumber(
      BusinessNumberFindRequest businessNumberFindRequest) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findBySellerNameAndStoreTel(
                businessNumberFindRequest.getSellerName(), businessNumberFindRequest.getStoreTel())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Seller Name or Store Tel"));

    return franchiseeEntity.getBusinessNumber();
  }
}
