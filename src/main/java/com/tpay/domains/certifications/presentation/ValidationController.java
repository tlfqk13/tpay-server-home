package com.tpay.domains.certifications.presentation;

import com.tpay.domains.certifications.application.BusinessNumberValidationService;
import com.tpay.domains.certifications.application.EmployeeUserIdValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유효성 검사 관련, 중복된 메서드일 가능성이 많으므로 추후 개선해야함
 */
@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidationController {

    private final BusinessNumberValidationService businessNumberValidationService;
    private final EmployeeUserIdValidationService employeeUserIdValidationService;

    @GetMapping("/{businessNumber}")
    public ResponseEntity validCheckBusinessNumber(@PathVariable Long businessNumber) {
        businessNumberValidationService.valid(businessNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employee/{userId}")
    public ResponseEntity<Boolean> validCheckUserId(
        @PathVariable String userId) {
        boolean result = employeeUserIdValidationService.valid(userId);
        return ResponseEntity.ok(result);
    }
}
