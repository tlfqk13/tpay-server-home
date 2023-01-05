package com.tpay.domains.refund.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.OrderNotFoundException;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.pos.application.BarcodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundBarcodeService {

    private final OrderRepository orderRepository;
    private final BarcodeService barcodeService;

    public void saveAndCreateBarcode(Long orderId){

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(ExceptionState.ORDER_NOT_FOUND)
                );

        String uri = barcodeService.createBarcode(orderEntity.getOrderNumber(),orderId);

        log.trace(" @@ uri = {}", uri);

    }

}
