package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.auth.application.AccessTokenService;
import com.tpay.domains.auth.domain.EmployeeAccessTokenEntity;
import com.tpay.domains.auth.domain.EmployeeAccessTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenRepository;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.external.domain.ExternalRepository;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.push.application.NonBatchPushService;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.domain.RefundAfterEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundAfterDto;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundItemDto;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundApproveService {

    private final OrderSaveService orderSaveService;
    private final RefundService refundService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final FranchiseeFindService franchiseeFindService;
    private final ExternalRepository externalRepository;
    private final OrderService orderService;
    private final WebRequestUtil webRequestUtil;

    private final EmployeeFindService employeeFindService;
    private final NonBatchPushService nonBatchPushService;

    private final AccessTokenService accessTokenService;
    private final FranchiseeAccessTokenRepository franchiseeAccessTokenRepository;
    private final EmployeeAccessTokenRepository employeeAccessTokenRepository;

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
        log.debug("Order saved Id = {} ", orderEntity.getId());
        updateDeviceInfo(request, orderEntity);

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(request.getFranchiseeIndex());
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

            //2022/03/25 여권 스캔을 바코드모드로하고, 앱으로 승인진행할 때 이 플로우 탐
            Optional<ExternalRefundEntity> optionalExternalRefundEntity = externalRepository.findByRefundEntity(refundEntity);
            optionalExternalRefundEntity.ifPresent(externalRefundEntity -> externalRefundEntity.changeStatus(ExternalRefundStatus.APPROVE));

            pointScheduledChangeService.change(refundEntity, SignType.POSITIVE, franchiseeEntity.getBalancePercentage());

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

    @Transactional
    public void updateDeviceInfo(RefundSaveRequest request, OrderEntity orderEntity) {
        if (request.getUserSelector().equals(EMPLOYEE)) {
            EmployeeEntity employeeEntity = employeeFindService.findById(request.getEmployeeIndex())
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Employee not exists"));
            orderEntity.setEmployeeEntity(employeeEntity);

            EmployeeAccessTokenEntity employeeAccessTokenEntityOptional =
                    employeeAccessTokenRepository.findByEmployeeEntityId(request.getFranchiseeIndex())
                            .orElseThrow(NullPointerException::new);

            if (request.getDevice() == null) {
                log.trace("Employee Device info no save Device is Null");
            } else {
                employeeAccessTokenEntityOptional.updateDeviceInfo(
                        request.getDevice().getName(),
                        request.getDevice().getOs(),
                        request.getDevice().getAppVersion());
                log.trace("Employee Device info save");
            }
        }

        FranchiseeAccessTokenEntity franchiseeTokenEntity =
                franchiseeAccessTokenRepository.findByFranchiseeEntityId(request.getFranchiseeIndex())
                        .orElseThrow(NullPointerException::new);
        if (request.getDevice() == null) {
            log.trace("Franchisee Device info no save Device is Null");
        } else {
            franchiseeTokenEntity.updateDeviceInfo(
                    request.getDevice().getName(),
                    request.getDevice().getOs(),
                    request.getDevice().getAppVersion());
            log.trace("Franchisee Device info save");
        }
    }

    @Transactional
    public RefundResponse approveAfter(RefundAfterDto.Request refundAfterDto) {
        OrderEntity orderEntity = orderService.findOrderByPurchaseSn(refundAfterDto.getRefundItem().getDocId());
        RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity, refundAfterDto);

        RefundResponse refundResponse;
        String uri = CustomValue.REFUND_SERVER + "/refund/after/approval";
        try {
            refundResponse = webRequestUtil.post(uri, refundApproveRequest);
        } catch (WebfluxGeneralException e) {
            log.debug("WEBFLUX_GENERAL_ERROR");
            throw new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, e.getMessage());
        }

        RefundEntity refundEntity =
                refundService.save(
                        refundResponse.getResponseCode(),
                        refundResponse.getTakeoutNumber(),
                        orderEntity);

        RefundAfterEntity refundAfterEntity = RefundAfterEntity.builder()
                .cusCode(refundAfterDto.getRefundAfterInfo().getCusCode())
                .localCode(refundAfterDto.getRefundAfterInfo().getLocaCode())
                .kioskBsnmCode(refundApproveRequest.getKioskBsnmCode())
                .kioskCode(refundAfterDto.getRefundAfterInfo().getKioskCode())
                .cityRefundCenterCode(refundApproveRequest.getCityRefundCenterCode())
                .refundAfterMethod(refundAfterDto.getRefundAfterInfo().getRefundAfterMethod())
                .build();

        refundEntity.addRefundAfterEntity(refundAfterEntity);
        return refundResponse;
    }
}
