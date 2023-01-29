package com.tpay.domains.van.presentation;

import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.dto.RefundAfterDto;
import com.tpay.domains.refund_core.application.dto.RefundItemDto;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import com.tpay.domains.van.domain.PaymentEntity;
import com.tpay.domains.van.domain.dto.VanRefundAfterDto;
import com.tpay.domains.van.domain.dto.VanRefundFinalDto;
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
    public ResponseEntity<String> vanRefundAfter(@RequestBody VanRefundAfterDto vanRefundDto) {
        log.debug("vanRefundDto = {}", vanRefundDto);
        PaymentEntity payment = PaymentEntity.builder()
                .paymentMethod(vanRefundDto.getPaymentMethod())
                .paymentBrand(vanRefundDto.getPaymentBrand())
                .paymentInfo(vanRefundDto.getPaymentInfo())
                .build();

        // 전표일련번호로 구매내역 확인
        List<RefundItemDto.Request> refundItems = vanRefundDto.getRefundItems();
        String responseCode = "0000";
        for (RefundItemDto.Request refundItem : refundItems) {
            RefundAfterDto.Request refundAfterDto = RefundAfterDto.Request.of(vanRefundDto.getRefundAfterBaseInfo(), refundItem);
            // payment info refund after 와 엮음
            try {
                RefundResponse refundResponse = refundService.approveAfter(refundAfterDto, payment);
                responseCode = refundResponse.getResponseCode();
            } catch (WebfluxGeneralException e) {
                // VAN 의 예외처리는 throw 가 아닌 에러코드와 함께 결과를 반환시킴
                String message = e.getMessage();
                String[] split = message.split(":");
                log.debug("split[0] = {}", split[0]);
                responseCode = split[0];
            }
        }

        return ResponseEntity.ok(responseCode);
    }

    @PostMapping("/approval/final")
    public ResponseEntity<String> vanRefundFinal(@RequestBody VanRefundFinalDto.Request vanFinalDto) {
        String responseCode = "0000";
        // 전표일련번호로 구매내역 확인
        List<RefundItemDto.Request> refundItems = vanFinalDto.getRefundItems();
        for (RefundItemDto.Request refundItem : refundItems) {
            try {
                RefundAfterDto.Request refundAfterDto = RefundAfterDto.Request.of(vanFinalDto, refundItem);
                RefundResponse refundResponse = refundService.approveAfter(refundAfterDto);
                responseCode = refundResponse.getResponseCode();
            } catch (WebfluxGeneralException e) {
                // VAN 의 예외처리는 throw 가 아닌 에러코드와 함께 결과를 반환시킴
                String message = e.getMessage();
                String[] split = message.split(":");
                log.debug("split[0] = {}", split[0]);
                responseCode = split[0];
            }
        }

        log.trace(" @@ vanRefundFinal.responseCode = {}", responseCode);
        return ResponseEntity.ok(responseCode);
    }

    @PostMapping("/approval/cancel")
    public ResponseEntity<String> vanRefundCancel(@RequestBody VanRefundAfterDto vanRefundDto) {
        log.debug("vanRefundDto = {}", vanRefundDto);
        PaymentEntity payment = PaymentEntity.builder()
                .paymentMethod(vanRefundDto.getPaymentMethod())
                .paymentBrand(vanRefundDto.getPaymentBrand())
                .paymentInfo(vanRefundDto.getPaymentInfo())
                .build();
        String responseCode = "0000";
        // 전표일련번호로 구매내역 확인
        List<RefundItemDto.Request> refundItems = vanRefundDto.getRefundItems();
        for (RefundItemDto.Request refundItem : refundItems) {
            RefundAfterDto.Request refundAfterDto = RefundAfterDto.Request.of(vanRefundDto.getRefundAfterBaseInfo(), refundItem);
            responseCode = refundService.approveCancel(refundAfterDto);
        }
        return ResponseEntity.ok(responseCode);
    }
}
