package com.tpay.domains.employee.presentation;


import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.application.dto.EmployeeFindResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeFindController {

  private final EmployeeFindService employeeFindService;

  @GetMapping("/employee/{franchiseeIndex}")
  public ResponseEntity<List<EmployeeFindResponseInterface>> findAllByFranchiseeId(@PathVariable Long franchiseeIndex) {
    List<EmployeeFindResponseInterface> result = employeeFindService.findAllByFranchiseeId(franchiseeIndex);
    return ResponseEntity.ok(result);
  }
}
