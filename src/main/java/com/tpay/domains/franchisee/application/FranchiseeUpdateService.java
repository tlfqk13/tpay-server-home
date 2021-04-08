package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeUpdateService {

  private final FranchiseeRepository franchiseeRepository;

  @Transactional
  public ResponseEntity<FranchiseeUpdateResponse> update(
      FranchiseeUpdateRequest franchiseeUpdateRequest) {
    FranchiseeEntity franchiseeEntity =
        franchiseeRepository
            .findById(franchiseeUpdateRequest.getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee ID"));

    franchiseeEntity.modifyInfo(
        franchiseeUpdateRequest.getStoreName(),
        franchiseeUpdateRequest.getStoreAddress(),
        franchiseeUpdateRequest.getBusinessNumber(),
        franchiseeUpdateRequest.getProductCategory());

    return ResponseEntity.ok(
        FranchiseeUpdateResponse.builder()
            .id(franchiseeEntity.getId())
            .storeName(franchiseeEntity.getStoreName())
            .storeAddress(franchiseeEntity.getStoreAddress())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .productCategory(franchiseeEntity.getProductCategory())
            .build());
  }
}
