package com.tpay.domains.order.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderApiService {

    private final OrderRepository orderRepository;

    public Optional<OrderEntity> findOrderByPurchaseSnInApi(String purchaseSn) {
        return orderRepository.findByOrderNumber(purchaseSn);
    }
}
