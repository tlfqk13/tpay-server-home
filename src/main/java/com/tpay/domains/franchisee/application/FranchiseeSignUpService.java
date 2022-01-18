package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantSaveService;
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
  private final FranchiseeApplicantSaveService franchiseeApplicantSaveService;

  @Transactional
  public void signUp(FranchiseeSignUpRequest request) {

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

    if (franchiseeRepository.existsByBusinessNumber(businessNumber.replaceAll("-", ""))) {
      throw new AlreadyExistsException(ExceptionState.ALREADY_EXISTS, "Franchisee Already Exists");
    }

    if (!regExUtils.validate(RegExType.EMAIL, request.getEmail())){
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Invalid Email Format");
    }

    String encodedPassword = passwordEncoder.encode(password);
    FranchiseeEntity franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber(request.getBusinessNumber())
            .storeName(request.getStoreName())
            .storeAddressNumber(request.getStoreAddressNumber())
            .storeAddressBasic(request.getStoreAddressBasic())
            .storeAddressDetail(request.getStoreAddressDetail())
            .sellerName(request.getSellerName())
            .storeTel(request.getStoreTel().replaceAll("-",""))
            .productCategory(request.getProductCategory())
            .password(encodedPassword)
            .signboard(request.getSignboard())
            .storeNumber(request.getStoreNumber().replaceAll("-",""))
            .email(request.getEmail())
            .build();
    franchiseeRepository.save(franchiseeEntity);
    franchiseeApplicantSaveService.save(franchiseeEntity);
  }
}
