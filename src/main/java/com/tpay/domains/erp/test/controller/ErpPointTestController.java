package com.tpay.domains.erp.test.controller;

import com.tpay.domains.erp.test.service.PointTestFindService;
import com.tpay.domains.point.application.PointUpdateService;
import com.tpay.domains.point.application.dto.AdminPointResponse;
import com.tpay.domains.point.application.dto.PointFindDetailResponse;
import com.tpay.domains.point.application.dto.PointUpdateRequest;
import com.tpay.domains.point.application.dto.WithdrawalStatus;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<AdminPointResponse> findPointsAdmin(
        @PathVariable Boolean isAll,
        @PathVariable WithdrawalStatus withdrawalStatus,
        @RequestParam int page,
        @RequestParam String searchKeyword) {
        AdminPointResponse pointsAdmin = pointTestFindService.findPointsAdmin(isAll, withdrawalStatus,page,searchKeyword);
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
