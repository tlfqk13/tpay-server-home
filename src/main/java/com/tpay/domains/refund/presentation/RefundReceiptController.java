package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundBarcodeService;
import com.tpay.domains.refund.application.RefundReceiptDownloadsService;
import com.tpay.domains.refund.application.RefundReceiptFindService;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/refund/receipt")
public class RefundReceiptController {

    private final RefundReceiptFindService refundReceiptFindService;
    private final RefundReceiptDownloadsService refundReceiptDownloadsService;
    private final RefundBarcodeService refundBarcodeService;

    @PostMapping("/passport-validate") // 여권번호를 입력해주세요
    public ResponseEntity<RefundReceiptDto.ResponseCustomer> findCustomer(
            @RequestBody RefundReceiptDto.Request request
    ){
        RefundReceiptDto.ResponseCustomer responseCustomer = refundReceiptFindService.findCustomer(request);
        return ResponseEntity.ok(responseCustomer);
    }

    @PatchMapping//출국 예정일을 입력해주세요
    public ResponseEntity<RefundReceiptDto.ResponseCustomer> updateDepartureDate(
            @RequestBody RefundReceiptDto.Request request
    ){
        RefundReceiptDto.ResponseCustomer responseCustomer = refundReceiptFindService.updateDepartureDate(request);
        return ResponseEntity.ok(responseCustomer);
    }

    @PostMapping
    public ResponseEntity<List<RefundReceiptDto.Response>> findRefundReceipt(
            @RequestBody RefundReceiptDto.Request request
    ) {
        List<RefundReceiptDto.Response> refundReceiptList = refundReceiptFindService.findRefundReceiptDetail(request);
        return ResponseEntity.ok(refundReceiptList);
    }

    @PostMapping("/downloads")
    public ResponseEntity<String> downloadsRefundReceipt(
            @RequestBody RefundReceiptDto.Request request
    ) {
        refundReceiptDownloadsService.downloadsRefundReceipt(request);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/barcode/{orderIndex}")
    public ResponseEntity<String> refundBarcode(
            @PathVariable Long orderIndex
    ) {
        refundBarcodeService.saveAndCreateBarcode(orderIndex);
        return ResponseEntity.ok("ok");
    }
}
