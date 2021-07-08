package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
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
  private final RegExUtils regExUtils;

  @Transactional
  public FranchiseeSignUpResponse signUp(FranchiseeSignUpRequest request) {

    String businessNumber = request.getBusinessNumber();
    if (!regExUtils.validate(RegExType.BUSINESS_NUMBER, businessNumber)) {
      throw new InvalidBusinessNumberException(
          ExceptionState.INVALID_BUSINESS_NUMBER,
          "Required Business Number Format : (XXX-XX-XXXXX)");
    }

    String password = request.getPassword();
    if (!regExUtils.validate(RegExType.PASSWORD, password)) {
      throw new InvalidPasswordException(
          ExceptionState.INVALID_PASSWORD, "Invalid Password Format");
    }

    if (franchiseeRepository.existsByBusinessNumber(businessNumber)) {
      throw new AlreadyExistsException(ExceptionState.ALREADY_EXISTS, "Franchisee Already Exists");
    }

    String encodedPassword = passwordEncoder.encode(password);
    FranchiseeEntity franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber(request.getBusinessNumber())
            .storeName(request.getStoreName())
            .storeAddress(request.getStoreAddress())
            .sellerName(request.getSellerName())
            .storeTel(request.getStoreTel())
            .productCategory(request.getProductCategory())
            .password(encodedPassword)
            .build();

    franchiseeRepository.save(franchiseeEntity);
    return FranchiseeSignUpResponse.builder().id(franchiseeEntity.getId()).build();
  }
}
