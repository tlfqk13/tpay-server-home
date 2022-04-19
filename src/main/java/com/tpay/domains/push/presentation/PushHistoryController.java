package com.tpay.domains.push.presentation;


import com.tpay.domains.push.application.PushHistoryService;
import com.tpay.domains.push.domain.PushHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PushHistoryController {

    private final PushHistoryService pushHistoryService;

    @GetMapping("/push/{franchiseeIndex")
    public ResponseEntity<List<PushHistoryEntity>> findByFranchiseeIndex(@PathVariable Long franchiseeIndex) {
        List<PushHistoryEntity> byFranchiseeIndex = pushHistoryService.findByFranchiseeIndex(franchiseeIndex);
        return ResponseEntity.ok(byFranchiseeIndex);
    }
}
