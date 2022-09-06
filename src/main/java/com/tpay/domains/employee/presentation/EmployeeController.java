package com.tpay.domains.employee.presentation;

import com.tpay.domains.employee.application.*;
import com.tpay.domains.employee.application.dto.*;
import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 직원 관련 CRUD, 세팅변경
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeFindService employeeFindService;
    private final EmployeeRegistrationService employeeRegistrationService;
    private final EmployeeDeleteService employeeDeleteService;
    private final EmployeeUpdateService employeeUpdateService;
    private final EmployeeSettingService employeeSettingService;

    /**
     * 가맹점별 직원 목록 조회
     */
    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<List<EmployeeFindResponse>> findAllByFranchiseeId(@PathVariable Long franchiseeIndex) {
        List<EmployeeFindResponse> result = employeeFindService.findAllByFranchiseeId(franchiseeIndex);
        return ResponseEntity.ok(result);
    }

    /**
     * 직원 등록
     */
    @PostMapping("/{franchiseeIndex}")
    public ResponseEntity<EmployeeEntity> registration(
        @PathVariable Long franchiseeIndex,
        @RequestBody EmployeeRegistrationRequest employeeRegistrationRequest
    ) {
        EmployeeEntity result = employeeRegistrationService.registration(franchiseeIndex, employeeRegistrationRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 직원 삭제. 여러명 삭제가능
     */
    @DeleteMapping
    public ResponseEntity<Boolean> delete(@RequestBody EmployeeDeleteRequest employeeDeleteRequest) {
        employeeDeleteService.delete(employeeDeleteRequest);
        return ResponseEntity.ok(true);
    }

    /**
     * 직원 정보 수정
     */
    @PatchMapping("/{employeeIndex}")
    public ResponseEntity<Boolean> update(
        @PathVariable Long employeeIndex,
        @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {
        boolean result = employeeUpdateService.update(employeeIndex, employeeUpdateRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 직원 세팅 변경
     */
    @PatchMapping("/{employeeIndex}/settings")
    public ResponseEntity<EmployeeSettingDto.Response> changeSoundOrVibration(
        @PathVariable Long employeeIndex,
        @RequestBody EmployeeSettingDto.Request request
    ) {
        EmployeeSettingDto.Response response = employeeSettingService.changeSoundOrVibration(employeeIndex, request);
        return ResponseEntity.ok(response);
    }
}
