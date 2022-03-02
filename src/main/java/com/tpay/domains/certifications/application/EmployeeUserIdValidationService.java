package com.tpay.domains.certifications.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.domains.employee.domain.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeUserIdValidationService {

  private final EmployeeRepository employeeRepository;


  public boolean valid(Long franchiseeIndex, String userId) {
    if(employeeRepository.existsByFranchiseeEntityIdAndUserId(franchiseeIndex, userId)){
      System.out.println("Already Exists!");
      throw new AlreadyExistsException(ExceptionState.ALREADY_EXISTS,"Employee Id Already Exists");
    }
    return false;
  }
}
