package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundReceiptDownloadsService;
import com.tpay.domains.refund.application.RefundReceiptFindService;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RefundReceiptController {

    private final RefundReceiptFindService refundReceiptFindService;
    private final RefundReceiptDownloadsService refundReceiptDownloadsService;

    @PostMapping("/refund/receipt")
    public ResponseEntity<List<RefundReceiptDto.Response>> findRefundReceipt(
            @RequestBody RefundReceiptDto.Request request
    ){
        List<RefundReceiptDto.Response> refundReceiptList = refundReceiptFindService.findRefundReceiptDetail(request);
        return ResponseEntity.ok(refundReceiptList);
    }

    @PostMapping("/refund/receipt/downloads")
    public ResponseEntity<String> downloadsRefundReceipt(
            @RequestBody RefundReceiptDto.Request request
    ){
        refundReceiptDownloadsService.downloadsRefundReceipt(request);
        return ResponseEntity.ok("ok");
    }



}
