package com.tpay.domains.order.presentation;

import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.order.application.OrderFindService;
import com.tpay.domains.order.application.dto.OrderFindResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderFindController {
  private final OrderFindService orderFindService;

  @PostMapping("/refund")
  public ResponseEntity<List<OrderFindResponse>> findAll(
      @RequestBody RefundInquiryRequest refundInquiryRequest) {
    return ResponseEntity.ok(orderFindService.findAllSale(refundInquiryRequest));
  }
}
