package com.tpay.domains.employee.presentation;


import com.tpay.domains.employee.application.EmployeeDeleteService;
import com.tpay.domains.employee.application.dto.EmployeeDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeDeleteController {

    private final EmployeeDeleteService employeeDeleteService;

    @DeleteMapping("/employee")
    public ResponseEntity<Boolean> delete(@RequestBody EmployeeDeleteRequest employeeDeleteRequest) {
        employeeDeleteService.delete(employeeDeleteRequest);
        return ResponseEntity.ok(true);
    }
}
