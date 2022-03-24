package com.tpay.domains.certifications.presentation;


import com.tpay.domains.certifications.application.BusinessNumberValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BusinessNumberValidationController {


    private final BusinessNumberValidationService businessNumberValidationService;

    @GetMapping("/validate/{businessNumber}")
    public ResponseEntity validCheckBusinessNumber(@PathVariable Long businessNumber) {
        businessNumberValidationService.valid(businessNumber);
        return ResponseEntity.ok().build();
    }
}
