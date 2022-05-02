package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeService;
import com.tpay.domains.franchisee.application.dto.FindAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FranchiseeController {

    private final FranchiseeService franchiseeService;

    @GetMapping("/franchisee")
    public ResponseEntity<List<FindAllResponse>> findAll() {
        List<FindAllResponse> response = franchiseeService.findAll();
        return ResponseEntity.ok(response);
    }


}
