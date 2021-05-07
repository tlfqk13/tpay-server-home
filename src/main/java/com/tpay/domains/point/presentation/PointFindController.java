package com.tpay.domains.point.presentation;

import com.tpay.domains.point.application.PointFindService;
import com.tpay.domains.point.application.dto.PointRequest;
import com.tpay.domains.point.application.dto.PointResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointFindController {

  private final PointFindService pointFindService;

  @PostMapping("/points")
  public ResponseEntity<List<PointResponse>> findPoints(
      @RequestParam Integer page,
      @RequestParam Integer size,
      @RequestBody PointRequest pointRequest) {
    return pointFindService.findPoints(page, size, pointRequest);
  }
}
