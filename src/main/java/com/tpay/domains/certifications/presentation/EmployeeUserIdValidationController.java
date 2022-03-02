package com.tpay.domains.certifications.presentation;


import com.tpay.domains.certifications.application.EmployeeUserIdValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeUserIdValidationController {

  private final EmployeeUserIdValidationService employeeUserIdValidationService;

  @GetMapping("/validate/employee/{franchiseeIndex}/{userId}")
  public ResponseEntity<Boolean> validCheckUserId(
      @PathVariable Long franchiseeIndex,
      @PathVariable String userId) {
    boolean result = employeeUserIdValidationService.valid(franchiseeIndex, userId);
    return ResponseEntity.ok(result);
  }
}