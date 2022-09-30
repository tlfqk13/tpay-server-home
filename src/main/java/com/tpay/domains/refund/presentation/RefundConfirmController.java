package com.tpay.domains.refund.presentation;

import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.application.dto.RefundConfirmDto;
import com.tpay.domains.refund.domain.RefundEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundConfirmController {

    private final OrderService orderService;

    @PostMapping("/refunds/confirm")
    public ResponseEntity<String> confirm(@RequestBody RefundConfirmDto confirmDto) {

        OrderEntity orderByPurchaseSn = orderService.findOrderByPurchaseSn(confirmDto.getPurchaseSn());
        RefundEntity refundEntity = orderByPurchaseSn.getRefundEntity();

        refundEntity.updateTakeOutInfo(confirmDto.getTkOutConfNum());

        return ResponseEntity.ok("ok");
    }
}
