package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
import com.tpay.domains.franchisee.application.dto.PasswordChangeRequest;
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


  public boolean selfCertification(String businessNumber, String name, String phoneNumber) {
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByBusinessNumber(businessNumber);
    certificationValid(franchiseeEntity, name, phoneNumber.replaceAll("-", ""));
    return true;
  }

  @Transactional
  public boolean reset(String businessNumber, PasswordChangeRequest passwordChangeRequest) {
    String newPassword = passwordChangeRequest.getNewPassword();
    String newPasswordCheck = passwordChangeRequest.getNewPasswordCheck();
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByBusinessNumber(businessNumber);
    passwordValid(newPassword,newPasswordCheck);
    franchiseeEntity.resetPassword(passwordEncoder.encode(newPassword));
    return true;
  }

  // 이 위는 로그아웃 상태에서, 이 아래는 로그인 상태에서

  public boolean correctPassword(Long franchiseeIndex, String password) {
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    if (!passwordEncoder.matches(password, franchiseeEntity.getPassword())) {
      throw new InvalidPasswordException(ExceptionState.INVALID_PASSWORD, "Mismatch between 'input Password' and 'franchisee Password'");
    }
    return true;
  }

  @Transactional
  public boolean change(Long franchiseeIndex, PasswordChangeRequest passwordChangeRequest) {
    String newPassword = passwordChangeRequest.getNewPassword();
    String newPasswordCheck = passwordChangeRequest.getNewPasswordCheck();
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    passwordValid(newPassword, newPasswordCheck);
    franchiseeEntity.resetPassword(passwordEncoder.encode(newPassword));
    return true;
  }

  // 내부용 메서드
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
}
