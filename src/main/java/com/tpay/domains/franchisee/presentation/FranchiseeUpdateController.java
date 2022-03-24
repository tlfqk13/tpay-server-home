package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeUpdateService;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeUpdateController {

    private final FranchiseeUpdateService franchiseeUpdateService;

    @PatchMapping("/franchisee/{franchiseeIndex}")
    public ResponseEntity<FranchiseeUpdateInfo> update(
        @PathVariable Long franchiseeIndex, @RequestBody FranchiseeUpdateInfo request) {
        FranchiseeUpdateInfo response = franchiseeUpdateService.update(franchiseeIndex, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/franchisee/{franchiseeIndex}/popUp")
    public ResponseEntity<Boolean> updatePopUp(@PathVariable Long franchiseeIndex) {
        boolean result = franchiseeUpdateService.updatePopUp(franchiseeIndex);
        return ResponseEntity.ok(result);
    }
}
