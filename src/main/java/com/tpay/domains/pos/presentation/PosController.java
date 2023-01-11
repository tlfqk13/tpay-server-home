package com.tpay.domains.pos.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.pos.application.PosBarcodeService;
import com.tpay.domains.pos.application.PosService;
import com.tpay.domains.pos.application.dto.PosBarcodeResponse;
import com.tpay.domains.pos.domain.UpdatePosTypeDto;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 포스기 관련
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pos")
public class PosController {

    private final PosService posService;
    private final PosBarcodeService posBarcodeService;

    @PatchMapping
    public ResponseEntity<Boolean> updatePosType(
            @RequestBody UpdatePosTypeDto.Request updatePosTypeDto,
            @KtpIndexInfo IndexInfo indexInfo) {

        Boolean result = posService.updatePosType(indexInfo.getIndex(), updatePosTypeDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refund/limit")
    public ResponseEntity<PosBarcodeResponse> createBarcode(
            @RequestBody RefundLimitRequest refundLimitRequest,
            @KtpIndexInfo IndexInfo indexInfo) {
        PosBarcodeResponse posBarcodeResponse = posBarcodeService.saveAndCreateBarcode(indexInfo.getIndex(), refundLimitRequest);
        return ResponseEntity.ok(posBarcodeResponse);
    }
}
