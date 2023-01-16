package com.tpay.domains.tourcash.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.push.application.NonBatchPushService;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TourcashRefundService {

    private final OrderSaveService orderSaveService;
    private final RefundService refundService;
    private final FranchiseeFindService franchiseeFindService;
    private final OrderService orderService;
    private final WebRequestUtil webRequestUtil;
    private final EmployeeFindService employeeFindService;
    private final NonBatchPushService nonBatchPushService;

    @Transactional
    public RefundResponse approve(RefundSaveRequest request, IndexInfo indexInfo) {
        Long franchiseeIndex = getFranchiseeIndex(indexInfo);
        OrderEntity orderEntity = orderSaveService.save(request, franchiseeIndex);


        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity);

        String uri = CustomValue.REFUND_SERVER + "/refund/approval";
        try {
            RefundResponse refundResponse = webRequestUtil.post(uri, refundApproveRequest);

            // TODO : 응답코드 "0000" 아닐시 테스트 필요
            RefundEntity refundEntity =
                    refundService.save(
                            refundResponse.getResponseCode(),
                            refundResponse.getPurchaseSequenceNumber(),
                            refundResponse.getTakeoutNumber(),
                            orderEntity);

            log.debug("Refund approve entity id = {} ", refundEntity.getId());

            if (!franchiseeEntity.getIsRefundOnce()) {
                nonBatchPushService.nonBatchPushNSave(PushCategoryType.CASE_FIVE, franchiseeEntity.getId());
                franchiseeEntity.isRefundOnce();
            }

            return refundResponse;

        } catch (WebfluxGeneralException e) {
            log.debug("Refund delete orderEntity id = {} ", orderEntity.getId());
            orderService.deleteByIndex(orderEntity.getId());
            log.debug("WEBFLUX_GENERAL_ERROR");
            throw new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, e.getMessage());
        }
    }

    private Long getFranchiseeIndex(IndexInfo indexInfo) {
        if (EMPLOYEE == indexInfo.getUserSelector()) {
            EmployeeEntity employeeEntity = employeeFindService.findById(indexInfo.getIndex())
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Employee not exists"));
            return employeeEntity.getFranchiseeEntity().getId();
        }

        return indexInfo.getIndex();
    }
}
