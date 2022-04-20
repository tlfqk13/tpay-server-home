package com.tpay.domains.employee.presentation;


import com.tpay.domains.employee.application.EmployeeSettingService;
import com.tpay.domains.employee.application.dto.EmployeeSettingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeSettingController {

    private final EmployeeSettingService employeeSettingService;

    @PatchMapping("/employee/{employeeIndex}/settings")
    public ResponseEntity<EmployeeSettingDto.Response> changeSoundOrVibration(
        @PathVariable Long employeeIndex,
        @RequestBody EmployeeSettingDto.Request request
    ) {
        EmployeeSettingDto.Response response = employeeSettingService.changeSoundOrVibration(employeeIndex, request);
        return ResponseEntity.ok(response);
    }
}
