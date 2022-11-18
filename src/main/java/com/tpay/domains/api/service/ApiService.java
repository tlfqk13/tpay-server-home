package com.tpay.domains.api.service;

import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.KtpApiException;
import com.tpay.domains.api.domain.vo.ApprovalDto;
import com.tpay.domains.api.domain.vo.CancelDto;
import com.tpay.domains.customer.application.CustomerApiService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.order.application.OrderApiService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.refund_core.application.RefundApiService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ApiService {

    private final CustomerApiService customerService;
    private final OrderApiService orderService;
    private final RefundApiService refundApiService;

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
    public RefundResponse approveRefund(Long customerIdx, ApprovalDto.Request request) {
        return refundApiService.approve(customerIdx, request);
    }

    /**
     * 일반적인 API 사용자들이 환급 취소를 위해 사용
     */
    @Transactional
    public void cancelRefund(String purchaseNumber) {
        Optional<OrderEntity> optionalOrder = orderService.findOrderByPurchaseSn(purchaseNumber);

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

        refundApiService.cancel(customerId, refundId);
    }

    /**
     * 환급 승인은 되었지만, 승인 확인이 되지 않은 주문들을 한번에 처리하기 위해
     * 물론 전문은 개별적으로 나갈 수 밖에 없음
     */
    @Transactional
    public List<RefundResponse> cancelRefund(List<CancelDto.Request> dtos) {
        List<String> purchaseNumbers = dtos.stream()
                .map(CancelDto.Request::getPurchaseSequenceNumber)
                .collect(Collectors.toList());

        List<OrderEntity> orders = orderService.findOrdersByPurchaseSn(purchaseNumbers);
        if (orders.isEmpty()) {
            throw new KtpApiException("0012", "구매일련번호로 찾을 수 있는 환급내역이 존재하지 않습니다");
        }

        List<RefundResponse> results = new ArrayList<>();
        List<Long> updateRefundIds = new ArrayList<>();
        for (OrderEntity order : orders) {
            if (RefundStatus.CANCEL == order.getRefundEntity().getRefundStatus()) {
                continue;
            }

            Long customerId = order.getCustomerEntity().getId();
            Long refundId = order.getRefundEntity().getId();
            log.debug("refundId = {}, customerId = {}", refundId, customerId);
            String purchaseSequenceNumber = order.getOrderNumber();
            try {
                String responseCode = refundApiService.cancelBulk(customerId, refundId);
                results.add(
                        RefundResponse.builder()
                                .responseCode(responseCode)
                                .purchaseSequenceNumber(purchaseSequenceNumber)
                                .build()
                );
                log.debug("Canceled purchaseSequenceNumber = {}", purchaseSequenceNumber);
                updateRefundIds.add(refundId);
            } catch (KtpApiException e) {
                String[] split = e.getMessage().split(":");
                RefundResponse response = RefundResponse.builder().responseCode(split[0]).build();
                results.add(response);
                log.error("Cannot update cancel purchaseSequenceNumber = {}", purchaseSequenceNumber);
            }
        }

        if (!updateRefundIds.isEmpty()) {
            int count = refundApiService.updateBulkRefundCancel(updateRefundIds);
            log.debug("Refund cancel affected count = {}", count);
        }

        return results;
    }
}
