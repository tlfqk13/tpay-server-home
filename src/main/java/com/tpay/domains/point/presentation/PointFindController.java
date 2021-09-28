package com.tpay.domains.point.presentation;

import com.tpay.domains.point.application.PointFindService;
import com.tpay.domains.point.application.dto.PointFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
