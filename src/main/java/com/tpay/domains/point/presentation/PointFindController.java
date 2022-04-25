package com.tpay.domains.point.presentation;

import com.tpay.domains.point.application.PointFindService;
import com.tpay.domains.point.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PointFindController {

    private final PointFindService pointFindService;

    @GetMapping("/points/franchisee/{franchiseeIndex}")
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

    @GetMapping("/points/franchisee/{franchiseeIndex}/total")
    public ResponseEntity<PointTotalResponseInterface> findPointsTotal(@PathVariable Long franchiseeIndex) {
        PointTotalResponseInterface pointTotalResponseInterface = pointFindService.findPointsTotal(franchiseeIndex);
        return ResponseEntity.ok(pointTotalResponseInterface);
    }

    @GetMapping("/admin/points/{isAll}/{withdrawalStatus}")
    public ResponseEntity<List<AdminPointResponse>> findPointsAdmin(
        @PathVariable Boolean isAll,
        @PathVariable WithdrawalStatus withdrawalStatus
    ) {
        List<AdminPointResponse> pointsAdmin = pointFindService.findPointsAdmin(isAll, withdrawalStatus);
        return ResponseEntity.ok(pointsAdmin);
    }

    @GetMapping("/admin/detail/points/{pointsIndex}")
    public ResponseEntity<PointFindDetailResponse> findPointsAdminDetail(@PathVariable Long pointsIndex) {
        PointFindDetailResponse pointFindDetailResponse = pointFindService.findDetailByIndex(pointsIndex);
        return ResponseEntity.ok(pointFindDetailResponse);
    }
}
