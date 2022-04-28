package com.tpay.domains.barcode.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.commons.webClient.WebRequestToRefund;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.external.domain.ExternalRepository;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static com.tpay.commons.custom.CustomValue.BARCODE_SAVE_PATH;
import static com.tpay.commons.custom.CustomValue.REFUND_SERVER;

@Service
@RequiredArgsConstructor
public class BarcodeCreateService {

    private final CustomerFindService customerFindService;
    private final ExternalRepository externalRepository;
    private final WebRequestToRefund webRequestToRefund;

    @Transactional
    public ResponseEntity<Resource> createBarCode(Long franchiseeIndex, RefundLimitRequest request) {

        String uri = REFUND_SERVER + "/refund/limit";
        CustomerEntity customerEntity = customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());

        ObjectMapper objectMapper = new ObjectMapper();
        Object post = webRequestToRefund.post(uri, request);
        RefundResponse refundResponse = objectMapper.convertValue(post, RefundResponse.class);

        refundResponse.addCustomerInfo(customerEntity.getId());
        ExternalRefundEntity externalRefundEntity = ExternalRefundEntity.builder()
            .customerIndex(refundResponse.getCustomerIndex())
            .franchiseeIndex(franchiseeIndex)
            .externalRefundStatus(ExternalRefundStatus.SCAN)
            .build();
        ExternalRefundEntity save = externalRepository.save(externalRefundEntity);
        String deductionPadding = refundResponse.getBeforeDeduction().substring(3);
        String idPadding = setWithZero(save.getId().toString(), 7);

        try {
            Barcode barcode = BarcodeFactory.createCode128B(idPadding + deductionPadding);
            String filename = LocalDateTime.now() + "_" + save.getId() + ".png";
            File file = new File(BARCODE_SAVE_PATH + filename);
            BarcodeImageHandler.savePNG(barcode, file);
            Resource resource = new FileSystemResource(BARCODE_SAVE_PATH + filename);

            HttpHeaders headers = new HttpHeaders();
            Path filePath = Paths.get(BARCODE_SAVE_PATH + filename);
            headers.add("Content-Type", Files.probeContentType(filePath));
            return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);

        } catch (Exception e) {
            throw new IllegalArgumentException("Barcode Create Fail");
        }
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

