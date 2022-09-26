package com.tpay.domains.refund_core.presentation;

import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.application.VanOrderService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund_core.application.dto.VanDocumentDto;
import com.tpay.domains.refund_core.application.dto.VanRefundDto;
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

    private final VanOrderService vanOrderService;

    @PostMapping("/approval")
    public ResponseEntity<String> vanRefundAfter(@RequestBody VanRefundDto.Request vanRefundDto) {

        // 전표일련번호로 구매내역 확인
        for (VanDocumentDto.Request vanDocument : vanRefundDto.getVanDocuments()) {
            String docId = vanDocument.getDocId();
            OrderEntity order = vanOrderService.getOrderEntityByDocId(docId);

        }

        // payment info refund after 와 엮음

        return ResponseEntity.ok("ok");
    }
}
