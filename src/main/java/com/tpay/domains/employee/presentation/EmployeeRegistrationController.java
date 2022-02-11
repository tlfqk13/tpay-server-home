package com.tpay.domains.employee.presentation;


import com.tpay.domains.employee.application.EmployeeRegistrationService;
import com.tpay.domains.employee.application.dto.EmployeeRegistrationRequest;
import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeRegistrationController {

  private final EmployeeRegistrationService employeeRegistrationService;

  @PostMapping("/employee/{franchiseeIndex}")
  public ResponseEntity<EmployeeEntity> registration(
      @PathVariable Long franchiseeIndex,
      @RequestBody EmployeeRegistrationRequest employeeRegistrationRequest
  ) {
    EmployeeEntity result = employeeRegistrationService.registration(franchiseeIndex, employeeRegistrationRequest);
    return ResponseEntity.ok(result);
  }
}
