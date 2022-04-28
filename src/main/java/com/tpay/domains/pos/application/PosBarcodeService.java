package com.tpay.domains.pos.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.commons.webClient.WebRequestToRefund;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.application.ExternalService;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.custom.CustomValue.REFUND_SERVER;

@Service
@RequiredArgsConstructor
public class PosBarcodeService {

    private final CustomerFindService customerFindService;
    private final ExternalService externalService;
    private final WebRequestToRefund webRequestToRefund;
    private final BarcodeService barcodeService;

    @Transactional
    public ResponseEntity<Resource> createBarCode(Long franchiseeIndex, RefundLimitRequest request) {

        //API
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = REFUND_SERVER + "/refund/limit";
        Object post = webRequestToRefund.post(uri, request);
        RefundResponse refundResponse = objectMapper.convertValue(post, RefundResponse.class);

        //외국인 정보 업데이트
        CustomerEntity customerEntity = customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());
        refundResponse.addCustomerInfo(customerEntity.getId());

        // ExternalRepository 등록
        ExternalRefundEntity save = externalService.save(franchiseeIndex, customerEntity.getId());

        //바코드 패딩설정
        String deductionPadding = refundResponse.getBeforeDeduction().substring(3);
        String idString = save.getId().toString();
        String idPadding = setWithZero(idString, 7);

        //바코드 생성 및 리턴
        return barcodeService.createResource(deductionPadding,idPadding,idString);

    }

    static String setWithZero(String target, Integer size) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder after = stringBuilder.append(target);
        if (after.length() <= size) {
            while (after.length() < size) {
                after.insert(0, "0");
            }
        } else {
            throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSWORD, "too long target to padding(target > size)");
        }
        return after.toString();
    }
}

