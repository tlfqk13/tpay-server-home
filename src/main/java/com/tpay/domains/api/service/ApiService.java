package com.tpay.domains.api.service;

import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.KtpApiException;
import com.tpay.domains.api.domain.vo.ApprovalDto;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.refund_core.application.RefundCancelService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ApiService {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final RefundCancelService refundCancelService;

    @Transactional
    public Long createCustomer(ApprovalDto.Request dto) {
        Optional<CustomerEntity> customerOptional;
        try {
            customerOptional = customerService.findCustomerByNationAndPassportNumber(dto.getPassport(), dto.getNation());
        } catch (InvalidParameterException e) {
            throw new KtpApiException("7012", "여권번호가 올바르게 암호화되지 않았습니다");
        }

        if (customerOptional.isPresent()) {
            return customerOptional.get().getId();
        } else {
            CustomerEntity customerEntity = customerService.updateCustomerInfo(dto.getName(), dto.getPassport(), dto.getNation());
            return customerEntity.getId();
        }
    }

    @Transactional
    public RefundResponse cancelRefund(String purchaseNumber) {
        Optional<OrderEntity> optionalOrder = orderService.findOrderByPurchaseSnInApi(purchaseNumber);

        if (optionalOrder.isEmpty()) {
            throw new KtpApiException("0012", "구매일련번호로 찾을 수 있는 환급내역이 존재하지 않습니다");
        }

        OrderEntity order = optionalOrder.get();
        if (RefundStatus.CANCEL == order.getRefundEntity().getRefundStatus()) {
            throw new KtpApiException("1003", "이미 취소된 물품판매 번호입니다");
        }

        Long customerId = order.getCustomerEntity().getId();
        Long refundId = order.getRefundEntity().getId();
        log.debug("refundId = {}, customerId = {}", refundId, customerId);

        return refundCancelService.cancel(customerId, refundId);
    }
}
