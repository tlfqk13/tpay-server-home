package com.tpay.domains.order.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VanOrderService {

    private final OrderRepository orderRepository;

    public OrderEntity getOrderEntityByDocId(String docId) {
        // Exception 추가
        return orderRepository.findByOrderNumber(docId).orElseThrow();
    }
}
