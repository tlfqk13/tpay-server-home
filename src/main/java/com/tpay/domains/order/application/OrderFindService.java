package com.tpay.domains.order.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderFindService {

    private final OrderRepository orderRepository;

    public OrderEntity findById(Long orderIndex) {
        return orderRepository.findById(orderIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "OrderId not exists"));
    }

    public OrderEntity findByFranchiseeId(Long franchiseeIndex) throws NullPointerException {
        return orderRepository.findByFranchiseeEntityId(franchiseeIndex).get();
    }
}
