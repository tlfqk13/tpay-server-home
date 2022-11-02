package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.webClient.WebRequestUtil;
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
import com.tpay.domains.refund_core.application.dto.RefundAfterBaseDto;
import com.tpay.domains.refund_core.application.dto.RefundAfterDto;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import com.tpay.domains.van.domain.PaymentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
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
    private final FranchiseeAccessTokenRepository franchiseeAccessTokenRepository;
    private final EmployeeAccessTokenRepository employeeAccessTokenRepository;

    @Transactional
    public RefundResponse approve(RefundSaveRequest request) {

        // TODO: 2022/10/11 가격 조회 - 30,000 미만일 경우 알기 위해서
        int checkMinPrice = Integer.parseInt(request.getPrice());
        if (checkMinPrice < 30000) {
            log.debug(" @@ Item Price = {}", request.getPrice());
            throw new InvalidParameterException(ExceptionState.CHECK_ITEM_PRICE);
        }

        validationCheckEmployee(request);

        OrderEntity orderEntity = orderSaveService.save(request);
        log.debug("Order saved Id = {} ", orderEntity.getId());
        log.trace(" @@ orderEntity = {}", orderEntity.getTotalRefund());

        updateUserDeviceInfo(request, orderEntity);

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

    /**
     * VAN 통한 사후환급 + VAN 없이 사후환급
     *
     * @param refundAfterDto 사후환급을 위해 필요 dto
     * @param payment        van 사용 시, 넘어오는 지급정보
     * @return
     */
    @Transactional
    public RefundResponse approveAfter(RefundAfterDto.Request refundAfterDto, PaymentEntity payment) {
        OrderEntity orderEntity = orderService.findOrderByPurchaseSn(refundAfterDto.getRefundItem().getDocId());
        RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity, refundAfterDto);

        RefundResponse refundResponse;
        String uri = CustomValue.REFUND_SERVER + "/refund/after/approval";
        refundResponse = webRequestUtil.post(uri, refundApproveRequest);

        // 기존 PRE_APPROVAL 환급 상태에서 재전송을 통해 반출번호와 상태 변경
        RefundAfterBaseDto refundAfterInfo = refundAfterDto.getRefundAfterInfo();
        if (refundAfterInfo.isRetry()) {
            RefundEntity existRefundEntity = orderEntity.getRefundEntity();
            existRefundEntity.updateTakeOutInfo(refundResponse.getTakeoutNumber(), refundAfterInfo.getRefundFinishDate());
            return refundResponse;
        }

        // VAN 통해 여권정보 매핑등의 전문 시, 사전에 생성될 수 있음
        if (null == orderEntity.getRefundEntity()) {
            RefundEntity refundEntity =
                    refundService.save(
                            refundResponse.getResponseCode(),
                            refundResponse.getTakeoutNumber(),
                            orderEntity);

            RefundAfterEntity refundAfterEntity = RefundAfterEntity.builder()
                    .cusCode(refundAfterInfo.getCusCode())
                    .locaCode(refundAfterInfo.getLocaCode())
                    .kioskBsnmCode(refundApproveRequest.getKioskBsnmCode())
                    .kioskCode(refundAfterInfo.getKioskCode())
                    .cityRefundCenterCode(refundApproveRequest.getCityRefundCenterCode())
                    .refundAfterMethod(refundAfterInfo.getRefundAfterMethod())
                    .build();


            refundEntity.addRefundAfterEntity(refundAfterEntity);
        }

        // VAN 으로 사전 생성된 엔티티들은 이미 환급을 위한 데이터 일부를 사전에 생성
        if (null != payment) {
            orderEntity.getRefundEntity().getRefundAfterEntity().addPayment(payment);
        }

        return refundResponse;
    }

    /**
     * 일반적인 사후환급 (VAN X)
     * webflux 를 실제 보내는 곳이 아닌 바깥쪽에서 catch 한 이유는
     * VAN 의 경우 error exception 이 아닌, 에러코드를 내려줘야하기 때문에
     * 서로 다르게 catch 해서 예외를 처리한다.
     */
    @Transactional
    public RefundResponse approveAfter(RefundAfterDto.Request refundAfterDto) {
        try {
            return approveAfter(refundAfterDto, null);
        } catch (WebfluxGeneralException e) {
            log.debug("WEBFLUX_GENERAL_ERROR");
            throw new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, e.getMessage());
        }
    }

    private void updateUserDeviceInfo(RefundSaveRequest request, OrderEntity orderEntity) {
        if (request.getUserSelector().equals(EMPLOYEE)) {
            EmployeeEntity employeeEntity = employeeFindService.findById(request.getEmployeeIndex())
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Employee not exists"));
            orderEntity.setEmployeeEntity(employeeEntity);

            EmployeeAccessTokenEntity employeeAccessTokenEntity =
                    employeeAccessTokenRepository.findByEmployeeEntityId(request.getEmployeeIndex())
                            .orElseThrow(NullPointerException::new);

            if (request.getDevice() == null) {
                log.warn("Employee Device info no save Device is Null");
            } else {
                employeeAccessTokenEntity.updateDeviceInfo(
                        request.getDevice().getName(),
                        request.getDevice().getOs(),
                        request.getDevice().getAppVersion());
                log.trace("Employee Device info save");
            }
        } else if (request.getUserSelector().equals(FRANCHISEE)) {
            FranchiseeAccessTokenEntity franchiseeTokenEntity =
                    franchiseeAccessTokenRepository.findByFranchiseeEntityId(request.getFranchiseeIndex())
                            .orElseThrow(NullPointerException::new);

            if (request.getDevice() == null) {
                log.warn("Franchisee Device info no save Device is Null");
            } else {
                franchiseeTokenEntity.updateDeviceInfo(
                        request.getDevice().getName(),
                        request.getDevice().getOs(),
                        request.getDevice().getAppVersion());
                log.trace("Franchisee Device info save");
            }
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "UserSelector must FRANCHISEE or EMPLOYEE");
        }
    }

    private void validationCheckEmployee(RefundSaveRequest request) {
        if (request.getUserSelector().equals(EMPLOYEE)) {
            EmployeeEntity employeeEntity = employeeFindService.findById(request.getEmployeeIndex())
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Employee not exists"));
            request.updateFranchiseeIndex(employeeEntity);
        } else if (!request.getUserSelector().equals(FRANCHISEE)) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "UserSelector must FRANCHISEE or EMPLOYEE");
        }
    }
}
