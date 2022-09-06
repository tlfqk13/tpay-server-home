package com.tpay.domains.point.presentation;

import com.tpay.domains.point.application.PointFindService;
import com.tpay.domains.point.application.PointWithdrawalService;
import com.tpay.domains.point.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 앱에서 포인트 관련 기능
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/points/franchisee")
public class PointController {

    private final PointFindService pointFindService;
    private final PointWithdrawalService pointWithdrawalService;

    /**
     * 포인트 조회 하단
     */
    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<PointFindResponse> findPoints(
        @PathVariable Long franchiseeIndex,
        @RequestParam Integer week,
        @RequestParam Integer month,
        @RequestParam Integer page,
        @RequestParam Integer size) {

        PointFindResponse response =
            pointFindService.findPoints(franchiseeIndex, week, month, page, size);

        return ResponseEntity.ok(response);
    }
    /**
     * 포인트 조회 상단
     */
    @GetMapping("/{franchiseeIndex}/total")
    public ResponseEntity<PointTotalResponseInterface> findPointsTotal(@PathVariable Long franchiseeIndex) {
        PointTotalResponseInterface pointTotalResponseInterface = pointFindService.findPointsTotal(franchiseeIndex);
        return ResponseEntity.ok(pointTotalResponseInterface);
    }

    /**
     * 포인트 출금 요청
     */
    @PostMapping("/{franchiseeIndex}")
    public ResponseEntity<PointWithdrawalResponse> pointWithdrawal(
        @PathVariable Long franchiseeIndex,
        @RequestBody PointWithdrawalRequest pointWithdrawalRequest) {
        PointWithdrawalResponse pointWithdrawalResponse = pointWithdrawalService.pointWithdrawal(franchiseeIndex, pointWithdrawalRequest);
        return ResponseEntity.ok(pointWithdrawalResponse);
    }
}
