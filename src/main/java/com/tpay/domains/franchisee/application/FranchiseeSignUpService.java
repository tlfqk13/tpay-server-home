package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpResponse;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeSignUpService {

  private final FranchiseeRepository franchiseeRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public FranchiseeSignUpResponse signUp(FranchiseeSignUpRequest request) {

    if (franchiseeRepository.existsByBusinessNumber(request.getBusinessNumber())) {
      throw new AlreadyExistsException(ExceptionState.ALREADY_EXISTS, "Franchisee Already Exists");
    }

    String password = passwordEncoder.encode(request.getPassword());
    FranchiseeEntity franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber(request.getBusinessNumber())
            .storeName(request.getStoreName())
            .storeAddress(request.getStoreAddress())
            .sellerName(request.getSellerName())
            .storeTel(request.getStoreTel())
            .productCategory(request.getProductCategory())
            .password(password)
            .build();

    franchiseeRepository.save(franchiseeEntity);
    return FranchiseeSignUpResponse.builder().id(franchiseeEntity.getId()).build();
  }
}
