package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundReceiptFindService;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RefundReceiptController {

    private final RefundReceiptFindService refundReceiptFindService;

    @PostMapping("/refund-receipt")
    public ResponseEntity<List<RefundReceiptDto>> findRefundReceipt(
            @RequestParam RefundReceiptDto.Request request
    ){
        List<RefundReceiptDto> refundReceiptList = refundReceiptFindService.findRefundReceiptDetail(request.getPassportNumber());
        return null;
    }
}
