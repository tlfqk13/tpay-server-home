package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
import com.tpay.domains.franchisee.application.dto.PasswordCorrectRequest;
import com.tpay.domains.franchisee.application.dto.PasswordResetRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

  private final FranchiseeFindService franchiseeFindService;
  private final FranchiseeRepository franchiseeRepository;
  private final PasswordEncoder passwordEncoder;
  private final RegExUtils regExUtils;

  public boolean existBusinessNumber(String businessNumber) {
    return franchiseeRepository.existsByBusinessNumber(businessNumber);
  }

  @Transactional
  public boolean reset(String businessNumber, PasswordResetRequest request) {

    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByBusinessNumber(businessNumber);
    certificationValid(franchiseeEntity, request.getName(), request.getPhoneNumber().replaceAll("-", ""));
    passwordValid(request.getNewPassword(), request.getNewPasswordCheck());

    franchiseeEntity.resetPassword(passwordEncoder.encode(request.getNewPassword()));
    return true;
  }

  private void certificationValid(FranchiseeEntity franchiseeEntity, String name, String phoneNumber) {
    if (!franchiseeEntity.isValidUser(name, phoneNumber)) {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Mismatch between BusinessNumber and Certification Info");
    }
  }

  private void passwordValid(String newPassword, String newPasswordCheck) {
    boolean regExCompile = regExUtils.validate(RegExType.PASSWORD, newPassword);
    boolean equals = newPassword.equals(newPasswordCheck);

    if (!(regExCompile && equals)) {
      throw new InvalidPasswordException(ExceptionState.INVALID_PASSWORD, "RegEx Or Equals Error");
    }
  }

  public boolean correctPassword(Long franchiseeIndex, PasswordCorrectRequest passwordCorrectRequest) {
    String password = passwordCorrectRequest.getPassword();
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    if(!passwordEncoder.matches(password, franchiseeEntity.getPassword())){
      throw new InvalidPasswordException(ExceptionState.INVALID_PASSWORD,"Mismatch Password");
    }
    return true;
  }
}
