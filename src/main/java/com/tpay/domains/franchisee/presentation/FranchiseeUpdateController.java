package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeUpdateController {

    private final FranchiseeUpdateService franchiseeUpdateService;

    @PatchMapping("/franchisee/{franchiseeIndex}/popUp")
    public ResponseEntity<Boolean> updatePopUp(@PathVariable Long franchiseeIndex) {
        boolean result = franchiseeUpdateService.updatePopUp(franchiseeIndex);
        return ResponseEntity.ok(result);
    }
}
