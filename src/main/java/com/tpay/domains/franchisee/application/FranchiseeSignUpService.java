package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.AlreadyExistsException;
import com.tpay.commons.exception.ErrorStatus;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeSignUpService {

  private final FranchiseeRepository franchiseeRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public ResponseEntity<FranchiseeSignUpResponse> signUp(
      FranchiseeSignUpRequest franchiseeSignUpRequest) {

    if (franchiseeRepository.existsByBusinessNumber(franchiseeSignUpRequest.getBusinessNumber())) {
      throw new AlreadyExistsException(ErrorStatus.ALREADY_EXISTS);
    }

    String password = passwordEncoder.encode(franchiseeSignUpRequest.getPassword());
    FranchiseeEntity franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber(franchiseeSignUpRequest.getBusinessNumber())
            .storeName(franchiseeSignUpRequest.getStoreName())
            .storeAddress(franchiseeSignUpRequest.getStoreAddress())
            .sellerName(franchiseeSignUpRequest.getSellerName())
            .storeTel(franchiseeSignUpRequest.getStoreTel())
            .productCategory(franchiseeSignUpRequest.getProductCategory())
            .password(password)
            .build();

    franchiseeRepository.save(franchiseeEntity);
    return ResponseEntity.ok(
        FranchiseeSignUpResponse.builder().id(franchiseeEntity.getId()).build());
  }
}
