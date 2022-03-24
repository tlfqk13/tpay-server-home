package com.tpay.domains.employee.presentation;


import com.tpay.domains.employee.application.EmployeeUpdateService;
import com.tpay.domains.employee.application.dto.EmployeeUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeUpdateController {

    private final EmployeeUpdateService employeeUpdateService;

    @PatchMapping("/employee/{employeeIndex}")
    public ResponseEntity<Boolean> update(
        @PathVariable Long employeeIndex,
        @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {
        boolean result = employeeUpdateService.update(employeeIndex, employeeUpdateRequest);
        return ResponseEntity.ok(result);
    }
}
