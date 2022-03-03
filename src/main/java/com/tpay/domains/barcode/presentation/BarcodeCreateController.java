package com.tpay.domains.barcode.presentation;


import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import lombok.RequiredArgsConstructor;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class BarcodeCreateController {

  @GetMapping("/barcode")
  public void barcodeMaker(
      @RequestBody RefundLimitRequest refundLimitRequest
      ) throws Exception {
    // TODO: 2022/03/03   여권번호 길이는 고정값으로하면 좋음(바코드 인식 등 오류 최소화)
    // 그러므로 최대 여권번호 길이 기반으로 하면 될 듯

    Barcode barcode = BarcodeFactory.createCode128("45670500000");
    Barcode barcode2 = BarcodeFactory.createCode128("M12345670 2500000");
    File file = new File("/Users/sunba/Desktop/testPNG.png");
    File file2 = new File("/Users/sunba/Desktop/test2PNG.png");
    BarcodeImageHandler.savePNG(barcode,file);
    BarcodeImageHandler.savePNG(barcode2,file2);
  }
}
