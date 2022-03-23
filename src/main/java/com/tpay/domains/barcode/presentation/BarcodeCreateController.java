package com.tpay.domains.barcode.presentation;


import com.tpay.domains.barcode.application.BarcodeCreateService;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BarcodeCreateController {

  private final BarcodeCreateService barcodeCreateService;

  @PostMapping("/pos/refund/limit")
  public void barcodeMaker(@RequestBody RefundLimitRequest refundLimitRequest) {
    barcodeCreateService.createBarCode(refundLimitRequest);
  }

}
