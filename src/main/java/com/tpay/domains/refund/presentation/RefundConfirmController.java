package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.application.dto.RefundConfirmDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundConfirmController {

    private final RefundService refundService;

    @PostMapping("/refunds/confirm")
    public void confirm(@RequestBody RefundConfirmDto confirmDto) {
        refundService.confirmRefundEntity(confirmDto.getPurchaseSn(), confirmDto.getTkOutConfNum());
    }
}
