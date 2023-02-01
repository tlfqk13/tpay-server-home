package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.auth.domain.EmployeeAccessTokenEntity;
import com.tpay.domains.auth.domain.EmployeeAccessTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenRepository;
import com.tpay.domains.barcode.application.BarcodeService;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.external.domain.ExternalRepository;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.application.dto.OrderDto;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.push.application.NonBatchPushService;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.domain.*;
import com.tpay.domains.refund_core.application.dto.*;
import com.tpay.domains.van.domain.PaymentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;

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
    private final CustomerService customerService;
    private final BarcodeService barcodeService;
    private final RefundCancelService cancelService;

    @Transactional
    public RefundResponse approve(RefundSaveRequest request, IndexInfo indexInfo) {

        checkMinPrice(request);
        checkKorCustomer(request);

        Long franchiseeIndex = getFranchiseeIndex(indexInfo);
        OrderEntity orderEntity = orderSaveService.save(request, franchiseeIndex);

        updateUserDeviceInfo(request, orderEntity, indexInfo);

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

            externalRefund(refundEntity);
            createPoint(refundEntity, franchiseeEntity);

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

    private void externalRefund(RefundEntity refundEntity) {
        //여권 스캔을 바코드모드로하고, 앱으로 승인진행할 때 이 플로우 탐
        Optional<ExternalRefundEntity> optionalExternalRefundEntity = externalRepository.findByRefundEntity(refundEntity);
        optionalExternalRefundEntity.ifPresent(externalRefundEntity -> externalRefundEntity.changeStatus(ExternalRefundStatus.APPROVE));
    }

    private void checkKorCustomer(RefundSaveRequest request) {
        CustomerEntity customerEntity = customerService.findByIndex(request.getCustomerIndex());
        if ("KOR".equals(customerEntity.getNation())) {
            log.trace(" @@ customerEntity.getNation() = {}", customerEntity.getNation());
            throw new InvalidParameterException(ExceptionState.KOR_CUSTOMER);
        }
    }

    private void checkMinPrice(RefundSaveRequest request) {
        int checkMinPrice = Integer.parseInt(request.getPrice());
        if (checkMinPrice < 30000) {
            log.debug(" @@ Item Price = {}", request.getPrice());
            throw new InvalidParameterException(ExceptionState.CHECK_ITEM_PRICE);
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

        RefundAfterBaseDto refundAfterInfo = refundAfterDto.getRefundAfterInfo();
        if (refundAfterInfo.isRetry()) {
            checkRefundStatus(orderEntity);
        } else if (null != payment) {
            checkRefundStatus(orderEntity);
        }
        checkOrderValidateDate(orderEntity);

        // VAN 으로 사전 생성된 엔티티들은 이미 환급을 위한 데이터 일부를 사전에 생성
        if (null != payment) {
            orderEntity.getRefundEntity().getRefundAfterEntity().addPayment(payment);
            createPoint(orderEntity.getRefundEntity(), orderEntity.getFranchiseeEntity());
            return RefundResponse.builder()
                    .responseCode("0000")
                    .build();
        }

        RefundResponse refundResponse;
        String uri = CustomValue.REFUND_SERVER + "/refund/after/approval";
        refundResponse = webRequestUtil.post(uri, refundApproveRequest);

        // 기존 PRE_APPROVAL 환급 상태에서 재전송을 통해 반출번호와 상태 변경
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

            log.trace(" @@ refundAfterEntity.getId() = {}", refundAfterEntity.getId());
            log.trace(" @@ refundEntity.getId() = {}", refundEntity.getId());
            refundEntity.addRefundAfterEntity(refundAfterEntity);

            updatePaymentStatus(refundEntity, refundAfterEntity);
            createPoint(refundEntity, orderEntity.getFranchiseeEntity());
            createBarcode(refundEntity);
        }

        RefundEntity existRefundEntity = orderEntity.getRefundEntity();
        existRefundEntity.updateTakeOutInfo(refundResponse.getTakeoutNumber(), refundAfterInfo.getRefundFinishDate());
        return refundResponse;
    }

    private void checkOrderValidateDate(OrderEntity orderEntity) {
        // 반출 유효기간 3개월 초과
        if (orderEntity.getCreatedDate().isBefore(LocalDateTime.now().minusMonths(3))) {
            throw new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, "MONTH");
        }
    }

    private void checkRefundStatus(OrderEntity orderEntity) {
        if (RefundStatus.CANCEL.equals(orderEntity.getRefundEntity().getRefundStatus())) {
            throw new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, "CANCEL");
        } else if (RefundStatus.APPROVAL.equals(orderEntity.getRefundEntity().getRefundStatus())) {
            throw new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, "APPROVAL");
        }
    }

    private void updatePaymentStatus(RefundEntity refundEntity, RefundAfterEntity refundAfterEntity) {
        if (refundEntity.getTakeOutNumber().contains("acpt")) {
            refundAfterEntity.updatePaymentStatus(PaymentStatus.PAYMENT_WAIT);
        }
    }

    private void createPoint(RefundEntity refundEntity, FranchiseeEntity franchiseeEntity) {
        pointScheduledChangeService.change(refundEntity, SignType.POSITIVE, franchiseeEntity.getBalancePercentage());
    }

    private void createBarcode(RefundEntity refundEntity) {
        barcodeService.createBarcode(refundEntity.getOrderEntity().getOrderNumber(), refundEntity.getOrderEntity().getId());
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

    /**
     * ㄹ
     * KTP에서 주문과 환급을 동시에 처리할 때 사용
     */
    @Transactional
    public RefundResponse approveAfter(OrderDto.Request orderDto, IndexInfo indexInfo) {
        try {
            // create Order
            OrderDto.Response order = orderSaveService.createOrder(orderDto, indexInfo);

            // build Refund After Dto
            RefundAfterBaseDto baseDto = RefundAfterBaseDto.builder()
                    .cusCode("040")
                    .refundAfterMethod(RefundAfterMethod.MANUAL)
                    .retry(false)
                    .build();
            RefundItemDto.Request refundItemDto = RefundItemDto.Request.builder().docId(order.getPurchaseSn()).build();
            RefundAfterDto.Request refundAfterDto = new RefundAfterDto.Request(baseDto, refundItemDto);

            return approveAfter(refundAfterDto);
        } catch (WebfluxGeneralException e) {
            log.debug("WEBFLUX_GENERAL_ERROR");
            throw new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, e.getMessage());
        }
    }

    public RefundResponse confirmApproveAfter(String purchaseSn) {
        OrderEntity order = orderService.findOrderByPurchaseSn(purchaseSn);
        RefundAfterBaseDto baseDto = RefundAfterBaseDto.builder()
                .cusCode("040")
                .refundAfterMethod(RefundAfterMethod.MANUAL)
                .retry(true)
                .build();
        RefundItemDto.Request refundItemDto = RefundItemDto.Request.builder().docId(order.getOrderNumber()).build();
        RefundAfterDto.Request refundAfterDto = new RefundAfterDto.Request(baseDto, refundItemDto);

        return approveAfter(refundAfterDto);
    }


    private void updateUserDeviceInfo(RefundSaveRequest request, OrderEntity orderEntity, IndexInfo indexInfo) {
        if (indexInfo.getUserSelector() == EMPLOYEE) {
            EmployeeAccessTokenEntity employeeAccessTokenEntity =
                    employeeAccessTokenRepository.findByEmployeeEntityId(indexInfo.getIndex())
                            .orElseThrow(NullPointerException::new);

            EmployeeEntity employeeEntity = employeeAccessTokenEntity.getEmployeeEntity();
            orderEntity.setEmployeeEntity(employeeEntity);

            updateEmployeeDeviceInfo(request, employeeAccessTokenEntity);
        } else {
            FranchiseeAccessTokenEntity franchiseeTokenEntity =
                    franchiseeAccessTokenRepository.findByFranchiseeEntityId(indexInfo.getIndex())
                            .orElseThrow(NullPointerException::new);

            updateFranchiseeDeviceInfo(request, franchiseeTokenEntity);
        }
    }

    private void updateEmployeeDeviceInfo(RefundSaveRequest request, EmployeeAccessTokenEntity employeeAccessTokenEntity) {
        if (request.getDevice() == null) {
            log.warn("Employee Device info no save Device is Null");
        } else {
            employeeAccessTokenEntity.updateDeviceInfo(
                    request.getDevice().getName(),
                    request.getDevice().getOs(),
                    request.getDevice().getAppVersion());
            log.trace("Employee Device info save");
        }
    }

    private void updateFranchiseeDeviceInfo(RefundSaveRequest request, FranchiseeAccessTokenEntity franchiseeTokenEntity) {
        if (request.getDevice() == null) {
            log.warn("Franchisee Device info no save Device is Null");
        } else {
            franchiseeTokenEntity.updateDeviceInfo(
                    request.getDevice().getName(),
                    request.getDevice().getOs(),
                    request.getDevice().getAppVersion());
            log.trace("Franchisee Device info save");
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


    @Transactional
    public String approveCancel(RefundAfterDto.Request refundAfterDto) {
        OrderEntity orderEntity = orderService.findOrderByPurchaseSn(refundAfterDto.getRefundItem().getDocId());
        checkOrderValidateDate(orderEntity);
        if (RefundStatus.CANCEL.equals(orderEntity.getRefundEntity().getRefundStatus())) {
            return "CANCEL";
        } else {
            log.trace(" @@ orderEntity = {}", orderEntity);
            return "0000";
        }
    }
}
