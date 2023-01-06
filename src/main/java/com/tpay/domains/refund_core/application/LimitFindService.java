package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.refund_core.application.dto.RefundCustomValue;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class LimitFindService {

    private final CustomerService customerService;
    private final WebRequestUtil webRequestUtil;

    private final FranchiseeRepository franchiseeRepository;

    @Transactional
    public RefundResponse find(RefundLimitRequest request) {
        String uri = CustomValue.REFUND_SERVER + "/refund/limit";

        // 독일 여권일 경우, D -> DEU
        if (checkNation(request)) {
            nationUpdate(request);
        }
        // 한도 조회 요청
        RefundResponse refundResponse = webRequestUtil.post(uri, request);
        log.debug("request.passportNumber = {} , refundResponse.getPassportNumber() = {}"
                , request.getPassportNumber()
                , refundResponse.getPassportNumber());

        log.debug("request.nation = {} , refundResponse.getNation() = {}"
                , request.getNationality()
                , refundResponse.getNationality());

        // 한도조회 스켄 or 수기 확인
        log.trace(" @@ request.getMethod = {}", request.getMethod());
        log.trace(" @@ request.getFranchiseeIndex = {}", request.getFranchiseeIndex());

        // 한도 조회 요청 후, 성공되면 고객 정보 등록
        if ((List.of("0000", "4008").contains(refundResponse.getResponseCode()))) {
            Long customerEntityId;
            Optional<CustomerEntity> customerEntityOptional = customerService.findCustomerByNationAndPassportNumber(refundResponse.getPassportNumber(), refundResponse.getNationality());
            CustomerEntity customerEntity;
            if (customerEntityOptional.isEmpty()) {
                customerEntity = customerService.updateCustomerInfo(request.getName(), refundResponse.getPassportNumber(), refundResponse.getNationality());
                customerEntityId = customerEntity.getId();
            } else {
                customerEntity = customerEntityOptional.get();
                customerEntityId = customerEntity.getId();
                if (customerEntity.getCustomerName().contentEquals(request.getName())) {
                    log.warn("saved name = {}, request name = {} is different", customerEntity.getCustomerName(), request.getName());
                }
            }

            log.debug("Refund Limit customerID = {}", customerEntityId);

            // 사후환급 신청 가맹점 여부 조회
            if (request.getFranchiseeIndex() != null) {
                FranchiseeEntity franchiseeEntity = franchiseeRepository.findById(request.getFranchiseeIndex())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Entity"));

                String refundStep = franchiseeEntity.getRefundStep();
                log.trace(" @@ refundStep = {}", refundStep);

                return refundResponse.addCustomerInfo(customerEntityId, refundStep);
            } else {
                return refundResponse.addCustomerInfo(customerEntityId);
            }

        } else {
            throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO, "한도조회 실패");
        }
    }

    private boolean checkNation(RefundLimitRequest request) {
        if (RefundCustomValue.NATION_GERMANY_D.equals(request.getNationality())) {
            log.debug(" @@ CheckNationValue = {}", request.getNationality());
            return true;
        } else {
            return false;
        }
    }

    private void nationUpdate(RefundLimitRequest request) {
        request.nationUpdate(RefundCustomValue.NATION_GERMANY_DEU);
    }
}
