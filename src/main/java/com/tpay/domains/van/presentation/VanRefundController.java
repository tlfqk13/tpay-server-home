package com.tpay.domains.van.presentation;

import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.dto.RefundAfterDto;
import com.tpay.domains.refund_core.application.dto.RefundItemDto;
import com.tpay.domains.van.domain.PaymentEntity;
import com.tpay.domains.van.domain.dto.VanRefundDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/van")
public class VanRefundController {

    private final RefundApproveService refundService;

    @PostMapping("/approval")
    public ResponseEntity<String> vanRefundAfter(@RequestBody VanRefundDto.Request vanRefundDto) {

        PaymentEntity payment = PaymentEntity.builder()
                .paymentMethod(vanRefundDto.getPaymentMethod())
                .paymentBrand(vanRefundDto.getPaymentBrand())
                .paymentInfo(vanRefundDto.getPaymentInfo())
                .build();

        // 전표일련번호로 구매내역 확인
        List<RefundItemDto.Request> refundItems = vanRefundDto.getRefundItems();
        for (RefundItemDto.Request refundItem : refundItems) {
            RefundAfterDto.Request refundAfterDto = new RefundAfterDto.Request(vanRefundDto.getRefundAfterBaseInfo(), refundItem);

            // payment info refund after 와 엮음
            refundService.approveAfter(refundAfterDto, payment);
        }

        return ResponseEntity.ok("ok");
    }
}
