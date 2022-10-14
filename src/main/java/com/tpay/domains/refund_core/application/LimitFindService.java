package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerUpdateService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund_core.application.dto.NationalCheckValue;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class LimitFindService {

    private final CustomerUpdateService customerUpdateService;
    private final WebRequestUtil webRequestUtil;

    @Transactional
    public RefundResponse find(RefundLimitRequest request) {
        String uri = CustomValue.REFUND_SERVER + "/refund/limit";

        // TODO: 2022/10/11 독일 여권일 경우, D -> DEU
        nationalCheck(request);

        // 한도 조회 요청
        RefundResponse refundResponse = webRequestUtil.post(uri, request);

        // 한도 조회 요청 후, 성공되면 고객 정보 등록
        if(refundResponse.getResponseCode().equals("0000")) {
            Long customerEntityId;
            Optional<CustomerEntity> customerEntityOptional = customerUpdateService.findCustomerByNationAndPassportNumber(request.getPassportNumber(), request.getNationality());
            if (customerEntityOptional.isEmpty()) {
                log.trace("Customer Not exists.");
                CustomerEntity customerEntity = customerUpdateService.updateCustomerInfo(request.getName(), request.getPassportNumber(), request.getNationality());
                customerEntityId = customerEntity.getId();
                log.trace("Refund Limit customerID = {}", customerEntityId);
            } else {
                log.trace("Customer already exists.");
                customerEntityId = customerEntityOptional.get().getId();
                log.trace("Refund Limit customerID = {}", customerEntityId);
            }

            return refundResponse.addCustomerInfo(customerEntityId);

        } else {
            throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO, "한도조회 실패");
        }
    }

    private void nationalCheck(RefundLimitRequest request) {
        if(NationalCheckValue.NATIONAL_GERMANY_D.equals(request.getNationality())){
            request.nationalUpdate(NationalCheckValue.NATIONAL_GERMANY_DEU);
            log.error(" @@ nationalCheck = {}", request.getNationality());
        }
    }
}
