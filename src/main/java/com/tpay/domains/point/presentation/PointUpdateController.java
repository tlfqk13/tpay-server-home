package com.tpay.domains.point.presentation;


import com.tpay.domains.point.application.PointUpdateService;
import com.tpay.domains.point.application.dto.PointUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointUpdateController {

  private final PointUpdateService pointUpdateService;

  @PatchMapping("/admin/points/{pointsIndex}")
  public ResponseEntity<String> updateStatus(
      @PathVariable Long pointsIndex,
      @RequestBody PointUpdateRequest pointUpdateRequest) {
    String result = pointUpdateService.updateStatus(pointsIndex, pointUpdateRequest);
    return ResponseEntity.ok(result);
  }
}
