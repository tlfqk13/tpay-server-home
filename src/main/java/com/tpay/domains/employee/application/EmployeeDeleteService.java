package com.tpay.domains.employee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeDeleteService {

  private final EmployeeFindService employeeFindService;

  @Transactional
  public void delete(Long employeeIndex) {
    EmployeeEntity employeeEntity = employeeFindService.findById(employeeIndex)
        .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "EmployeeIndex doesn't exist"));
    employeeEntity.updateDelete();
  }
}
