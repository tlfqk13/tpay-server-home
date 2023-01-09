package com.tpay.domains.erp.test.controller;

import com.tpay.domains.erp.test.service.PointTestFindService;
import com.tpay.domains.point.application.PointUpdateService;
import com.tpay.domains.point.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 포인트 관련 admin 기능
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test/admin/points")
public class ErpPointTestController {

    private final PointTestFindService pointTestFindService;
    private final PointUpdateService pointUpdateService;

    /**
     * 기본 조회
     */
    @GetMapping("/{isAll}/{withdrawalStatus}")
    public ResponseEntity<Page<AdminPointInfo>> findPointsAdmin(
            @PathVariable Boolean isAll,
            @PathVariable WithdrawalStatus withdrawalStatus,
            Pageable pageable,
            @RequestParam(defaultValue = "") String searchKeyword) {
        Page<AdminPointInfo> pointsAdmin = pointTestFindService.findPointsAdmin(pageable, isAll, withdrawalStatus, searchKeyword);
        return ResponseEntity.ok(pointsAdmin);
    }

    /**
     * 상세 조회
     */
    @GetMapping("/detail/{pointsIndex}")
    public ResponseEntity<PointFindDetailResponse> findPointsAdminDetail(@PathVariable Long pointsIndex) {
        PointFindDetailResponse pointFindDetailResponse = pointTestFindService.findDetailByIndex(pointsIndex);
        return ResponseEntity.ok(pointFindDetailResponse);
    }

    /**
     * 포인트 상태 변경
     */
    @PatchMapping("/{pointsIndex}")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long pointsIndex,
            @RequestBody PointUpdateRequest pointUpdateRequest) {
        String result = pointUpdateService.updateStatus(pointsIndex, pointUpdateRequest);
        return ResponseEntity.ok(result);
    }
}
