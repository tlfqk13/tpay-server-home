package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundFindService;
import com.tpay.domains.refund.application.dto.RefundFindResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RefundFindController {

  private final RefundFindService refundFindService;

  @GetMapping("/franchisee/{franchiseeIndex}/refunds")
  public ResponseEntity<List<RefundFindResponse>> findList(@PathVariable Long franchiseeIndex) {
    List<RefundFindResponse> responseList = refundFindService.findList(franchiseeIndex);
    return ResponseEntity.ok(responseList);
  }
}
