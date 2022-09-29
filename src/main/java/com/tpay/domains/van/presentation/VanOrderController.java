package com.tpay.domains.van.presentation;

import com.tpay.domains.van.application.VanService;
import com.tpay.domains.van.domain.dto.VanOrdersDto;
import com.tpay.domains.van.domain.dto.VanRefundAfterBaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/van")
@Slf4j
public class VanOrderController {

    private final VanService vanService;


    @PostMapping("/order")
    public ResponseEntity<VanOrdersDto.Response> ordersDetail(
            @RequestBody VanOrdersDto.Request request
    ) {
        log.trace("passportNumber = {}", request.getEncryptPassportNumber());
        VanRefundAfterBaseDto refundAfterBaseDto = request.getRefundAfterBaseDto();
        vanService.createRefundAfter(request.getEncryptPassportNumber(), refundAfterBaseDto);
        VanOrdersDto.Response response = vanService.findVanOrder(request.getEncryptPassportNumber());
        return ResponseEntity.ok(response);
    }
}
