package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.external.application.ExternalRefundFindService;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.external.domain.ExternalRepository;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.refund.application.RefundSaveService;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

import java.util.Optional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
public class RefundApproveService {

    private final OrderSaveService orderSaveService;
    private final RefundSaveService refundSaveService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final FranchiseeFindService franchiseeFindService;
    private final WebClient.Builder builder;
    private final ExternalRepository externalRepository;

    private final PointRepository pointRepository;
    private final EmployeeFindService employeeFindService;

    @Transactional
    public RefundResponse approve(RefundSaveRequest request) {

        if (request.getUserSelector().equals(EMPLOYEE)) {
            EmployeeEntity employeeEntity = employeeFindService.findById(request.getEmployeeIndex())
                .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Employee not exists"));
            request.updateFranchiseeIndex(employeeEntity);
        } else if (!request.getUserSelector().equals(FRANCHISEE)) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "UserSelector must FRANCHISEE or EMPLOYEE");
        }

        OrderEntity orderEntity = orderSaveService.save(request);

        if (request.getUserSelector().equals(EMPLOYEE)) {
            EmployeeEntity employeeEntity = employeeFindService.findById(request.getEmployeeIndex())
                .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Employee not exists"));
            orderEntity.setEmployeeEntity(employeeEntity);
        }

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(request.getFranchiseeIndex());
        RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity);


        WebClient webClient = builder.build();
        String uri = CustomValue.REFUND_SERVER + "/refund/approval";
        RefundResponse refundResponse =
            webClient
                .post()
                .uri(uri)
                .bodyValue(refundApproveRequest)
                .retrieve()
                .onStatus(
                    HttpStatus::isError,
                    response ->
                        response.bodyToMono(ExceptionResponse.class).flatMap(error -> Mono.error(new InvalidParameterException(
                            ExceptionState.REFUND, error.getMessage()))))
                .bodyToMono(RefundResponse.class)
                // .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
                .block();

        // TODO : 응답코드 "0000" 아닐시 테스트 필요
        RefundEntity refundEntity =
            refundSaveService.save(
                refundResponse.getResponseCode(),
                refundResponse.getPurchaseSequenceNumber(),
                refundResponse.getTakeoutNumber(),
                orderEntity);

        //2022/03/25 여권 스캔을 바코드모드로하고, 앱으로 승인진행할 때 이 플로우 탐
        Optional<ExternalRefundEntity> optionalExternalRefundEntity = externalRepository.findByRefundEntity(refundEntity);
        optionalExternalRefundEntity.ifPresent(externalRefundEntity -> externalRefundEntity.changeStatus(ExternalRefundStatus.APPROVE));


        pointScheduledChangeService.change(refundEntity, SignType.POSITIVE);
        franchiseeEntity.isRefundOnce();
        return refundResponse;
    }
}
