package com.tpay.domains.order.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderFindService {

    private final OrderRepository orderRepository;

    @Transactional
    public Long sumTotalSaleAmountByFranchiseeIndex(Long franchiseeIndex) {
        Optional<Long> optionalResult = orderRepository.sumTotalSaleAmountByFranchiseeIndex(franchiseeIndex);
        if (optionalResult.isEmpty()) {
            return 0L;
        } else return optionalResult.get();
    }

    public OrderEntity findById(Long orderIndex) {
        return orderRepository.findById(orderIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "OrderId not exists"));
    }
}
