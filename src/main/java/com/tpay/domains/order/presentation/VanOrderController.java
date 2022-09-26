package com.tpay.domains.order.presentation;

import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.application.dto.OrdersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/van")
@Slf4j
public class VanOrderController {

    private final OrderService orderService;

    @GetMapping("/order/passport")
    public ResponseEntity<OrdersDto.Response> ordersDetail(
            @RequestBody OrdersDto.Request request
    ) {
        log.trace("passportNumber = {}", request.getEncryptPassportNumber());
        OrdersDto.Response response = orderService.ordersDetail(request.getEncryptPassportNumber());
        return ResponseEntity.ok(response);
    }
}
