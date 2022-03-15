package com.tpay.domains.point.presentation;

import com.tpay.domains.point.application.PointWithdrawalService;
import com.tpay.domains.point.application.dto.PointWithdrawalRequest;
import com.tpay.domains.point.application.dto.PointWithdrawalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointWithdrawalController {

  private final PointWithdrawalService pointWithdrawalService;

  @PostMapping("/points/franchisee/{franchiseeIndex}")
  public ResponseEntity<PointWithdrawalResponse> pointWithdrawal(
      @PathVariable Long franchiseeIndex,
      @RequestBody PointWithdrawalRequest pointWithdrawalRequest) {
    PointWithdrawalResponse pointWithdrawalResponse = pointWithdrawalService.pointWithdrawal(franchiseeIndex, pointWithdrawalRequest);
    return ResponseEntity.ok(pointWithdrawalResponse);
  }
}
