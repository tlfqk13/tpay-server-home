package com.tpay.domains.push.presentation;


import com.tpay.domains.push.application.PushHistoryService;
import com.tpay.domains.push.application.dto.CountIsReadDto;
import com.tpay.domains.push.application.dto.UpdateIsReadDto;
import com.tpay.domains.push.domain.PushHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PushHistoryController {

    private final PushHistoryService pushHistoryService;

    @GetMapping("/push/{franchiseeIndex}")
    public ResponseEntity<List<PushHistoryEntity>> findByFranchiseeIndex(@PathVariable Long franchiseeIndex) {
        List<PushHistoryEntity> byFranchiseeIndex = pushHistoryService.findByFranchiseeIndex(franchiseeIndex);
        return ResponseEntity.ok(byFranchiseeIndex);
    }

    @GetMapping("/push/detail/{pushIndex}")
    public ResponseEntity<PushHistoryEntity> findByPushIndex(@PathVariable Long pushIndex) {
        PushHistoryEntity byPushIndex = pushHistoryService.findByPushIndex(pushIndex);
        return ResponseEntity.ok(byPushIndex);
    }

    @PatchMapping("/push/{pushIndex}")
    public void updateIsRead(
        @PathVariable Long pushIndex,
        @RequestBody UpdateIsReadDto updateIsReadDto) {
        pushHistoryService.updateIsRead(pushIndex, updateIsReadDto);
    }

    @GetMapping("/push/{franchiseeIndex}/count")
    public ResponseEntity<CountIsReadDto> countIsRead(
        @PathVariable Long franchiseeIndex
    ) {
        CountIsReadDto countIsReadDto = pushHistoryService.countIsRead(franchiseeIndex);
        return ResponseEntity.ok(countIsReadDto);
    }
}
