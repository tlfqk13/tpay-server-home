package com.tpay.domains.employee.presentation;


import com.tpay.domains.employee.application.EmployeeDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeDeleteController {

  private final EmployeeDeleteService employeeDeleteService;

  @DeleteMapping("/employee/{employeeIndex}")
  public ResponseEntity<Boolean> delete(@PathVariable Long employeeIndex){
    employeeDeleteService.delete(employeeIndex);
    return ResponseEntity.ok(true);
  }
}
