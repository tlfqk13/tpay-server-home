package com.tpay.domains.pos.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.application.ExternalService;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.pos.application.dto.PosBarcodeResponse;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.tpay.commons.custom.CustomValue.REFUND_SERVER;

@Service
@RequiredArgsConstructor
public class PosBarcodeService {

    private final CustomerFindService customerFindService;
    private final ExternalService externalService;
    private final WebRequestUtil webRequestUtil;
    private final BarcodeService barcodeService;

    @Transactional
    public PosBarcodeResponse saveAndCreateBarcode(Long franchiseeIndex, RefundLimitRequest request) {
        //Webflux - API
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = REFUND_SERVER + "/refund/limit";
        Object post = webRequestUtil.post(uri, request);

        //외국인 정보 업데이트
        RefundResponse refundResponse = objectMapper.convertValue(post, RefundResponse.class);
        CustomerEntity customerEntity = customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());

        //ExternalRepository 등록
        ExternalRefundEntity save = externalService.save(franchiseeIndex, customerEntity.getId(), refundResponse.getBeforeDeduction());
        Long id = save.getId();

        //바코드 생성 및 S3파일 업로드
        String s3Path = barcodeService.createBarcode(id, refundResponse.getBeforeDeduction());

        return PosBarcodeResponse.builder()
            .s3Path(s3Path)
            .externalRefundIndex(id)
            .build();

    }


}

