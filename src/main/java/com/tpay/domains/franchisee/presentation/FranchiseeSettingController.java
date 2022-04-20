package com.tpay.domains.franchisee.presentation;


import com.tpay.domains.franchisee.application.FranchiseeSettingService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSettingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeSettingController {

    private final FranchiseeSettingService franchiseeSettingService;

    @PatchMapping("/franchisee/{franchiseeIndex}/settings")
    public ResponseEntity<FranchiseeSettingDto.Response> changeSoundOrVibration(
        @PathVariable Long franchiseeIndex,
        @RequestBody FranchiseeSettingDto.Request request
    ) {
        FranchiseeSettingDto.Response response = franchiseeSettingService.changeSoundOrVibration(franchiseeIndex, request);
        return ResponseEntity.ok(response);
    }
}
