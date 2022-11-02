package com.tpay.domains.refund.presentation;

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

    @PostMapping("/refund/receipt")
    public ResponseEntity<List<RefundReceiptDto.Response>> findRefundReceipt(
            @RequestBody RefundReceiptDto.Request request
    ){
        List<RefundReceiptDto.Response> refundReceiptList = refundReceiptFindService.findRefundReceiptDetail(request);
        return ResponseEntity.ok(refundReceiptList);
    }


}
