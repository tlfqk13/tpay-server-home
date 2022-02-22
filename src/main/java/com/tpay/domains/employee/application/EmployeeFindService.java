package com.tpay.domains.employee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.employee.domain.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeFindService {

  private final EmployeeRepository employeeRepository;

  public EmployeeEntity findByUserId(String userId) {
    return employeeRepository.findByUserId(userId)
        .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "UserId Not Exists"));
  }

  public Optional<EmployeeEntity> findById(Long employeeIndex){
    return employeeRepository.findById(employeeIndex);
  }
}
