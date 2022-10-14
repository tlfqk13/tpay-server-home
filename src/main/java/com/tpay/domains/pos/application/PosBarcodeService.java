package com.tpay.domains.pos.application;

import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerUpdateService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.application.ExternalService;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.pos.application.dto.PosBarcodeResponse;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.tpay.commons.custom.CustomValue.REFUND_SERVER;

@Slf4j
@Service
@RequiredArgsConstructor
public class PosBarcodeService {

    private final CustomerUpdateService customerUpdateService;
    private final ExternalService externalService;
    private final WebRequestUtil webRequestUtil;
    private final BarcodeService barcodeService;

    @Transactional
    public PosBarcodeResponse saveAndCreateBarcode(Long franchiseeIndex, RefundLimitRequest request) {
        //Webflux - API
        String uri = REFUND_SERVER + "/refund/limit";
        RefundResponse refundResponse = webRequestUtil.post(uri, request);

        //외국인 정보 업데이트
        Optional<CustomerEntity> customerEntityOptional = customerUpdateService.findCustomerByNationAndPassportNumber(refundResponse.getPassportNumber(), request.getNationality());
        CustomerEntity customerEntity;
        if(customerEntityOptional.isEmpty()){
            customerEntity = customerUpdateService.updateCustomerInfo(request.getName(), refundResponse.getPassportNumber(), request.getNationality());
        } else {
            customerEntity = customerEntityOptional.get();
        }

        //ExternalRepository 등록
        ExternalRefundEntity save = externalService.save(franchiseeIndex, customerEntity.getId(), refundResponse.getBeforeDeduction());
        Long id = save.getId();

        //바코드 생성 및 S3파일 업로드
        String s3Path = barcodeService.createBarcode(id, refundResponse.getBeforeDeduction());

        log.trace("externalRefundIndex : {} 바코드가 정상적으로 생성되었습니다.", id);
        return PosBarcodeResponse.builder()
            .s3Path(s3Path)
            .externalRefundIndex(id)
            .build();

    }

}

