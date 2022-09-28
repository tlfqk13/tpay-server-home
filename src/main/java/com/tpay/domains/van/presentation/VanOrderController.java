package com.tpay.domains.van.presentation;

import com.tpay.domains.van.application.VanOrderService;
import com.tpay.domains.van.domain.dto.VanOrdersDto;
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

    private final VanOrderService vanOrderService;

    @PostMapping("/order")
    public ResponseEntity<VanOrdersDto.Response> ordersDetail(
            @RequestBody VanOrdersDto.Request request
    ) {
        log.trace("passportNumber = {}", request.getEncryptPassportNumber());
        VanOrdersDto.Response response = vanOrderService.findVanOrder(request.getEncryptPassportNumber());
        return ResponseEntity.ok(response);
    }
}
