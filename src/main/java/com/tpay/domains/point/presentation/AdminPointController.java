package com.tpay.domains.point.presentation;

import com.tpay.domains.point.application.PointFindService;
import com.tpay.domains.point.application.PointUpdateService;
import com.tpay.domains.point.application.dto.AdminPointResponse;
import com.tpay.domains.point.application.dto.PointFindDetailResponse;
import com.tpay.domains.point.application.dto.PointUpdateRequest;
import com.tpay.domains.point.application.dto.WithdrawalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 포인트 관련 admin 기능
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/points")
public class AdminPointController {

    private final PointFindService pointFindService;
    private final PointUpdateService pointUpdateService;

    /**
     * 기본 조회
     */
    @GetMapping("/{isAll}/{withdrawalStatus}")
    public ResponseEntity<List<AdminPointResponse>> findPointsAdmin(
        @PathVariable Boolean isAll,
        @PathVariable WithdrawalStatus withdrawalStatus
    ) {
        List<AdminPointResponse> pointsAdmin = pointFindService.findPointsAdmin(isAll, withdrawalStatus);
        return ResponseEntity.ok(pointsAdmin);
    }

    /**
     * 상세 조회
     */
    @GetMapping("/detail/{pointsIndex}")
    public ResponseEntity<PointFindDetailResponse> findPointsAdminDetail(@PathVariable Long pointsIndex) {
        PointFindDetailResponse pointFindDetailResponse = pointFindService.findDetailByIndex(pointsIndex);
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
