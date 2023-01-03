package com.tpay.domains.refund.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.OrderNotFoundException;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RefundFindService {

    private final RefundRepository refundRepository;

    public RefundEntity findById(Long refundIndex) {

        return refundRepository.findById(refundIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));
    }

    public List<RefundEntity> findAllByIdAndDate(Long refundIndex, LocalDateTime startDate, LocalDateTime endDate){

        return refundRepository.findAllByIdAndCreatedDateBetween(refundIndex,startDate,endDate);

    }

    public RefundEntity findByOrderEntity(OrderEntity orderEntity) throws NullPointerException{
        return refundRepository.findByOrderEntity(orderEntity).get();
    }

    public RefundEntity getRefundByTkOutNumber(String tkOutNum) {
        return refundRepository.findByTakeOutNumber(tkOutNum)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionState.ORDER_NOT_FOUND, "TkOutNumber 로 주문을 찾을 수 없음"));
    }

    public RefundEntity getRefundByRefundId(Long refundIndex) {
        return refundRepository.findById(refundIndex)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionState.ORDER_NOT_FOUND, "RefundId 로 주문을 찾을 수 없음"));
    }
}
