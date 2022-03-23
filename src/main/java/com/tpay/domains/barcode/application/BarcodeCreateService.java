package com.tpay.domains.barcode.application;

import com.amazonaws.util.IOUtils;
import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.io.File;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class BarcodeCreateService {

  private final WebClient.Builder builder;
  private final CustomerFindService customerFindService;
  private final ExternalRepository externalRepository;

  @Transactional
  public byte[] createBarCode(Long franchiseeIndex, RefundLimitRequest request) {
    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/refund/limit";
    CustomerEntity customerEntity = customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());

    RefundResponse refundResponse = webClient
        .post()
        .uri(uri)
        .bodyValue(request)
        .retrieve()
        .onStatus(
            HttpStatus::isError, response -> response.bodyToMono(ExceptionResponse.class)
                .flatMap(error -> Mono.error(new InvalidParameterException(ExceptionState.REFUND, error.getMessage()))))
        .bodyToMono(RefundResponse.class)
        .block();

    refundResponse.addCustomerInfo(customerEntity.getId());
    ExternalRefundEntity externalRefundEntity = ExternalRefundEntity.builder()
        .customerIndex(refundResponse.getCustomerIndex())
        .franchiseeIndex(franchiseeIndex)
        .externalRefundStatus(ExternalRefundStatus.SCAN)
        .build();
    ExternalRefundEntity save = externalRepository.save(externalRefundEntity);
    String deductionPadding = setWithZero(refundResponse.getBeforeDeduction(), 11);
    String idPadding = setWithZero(save.getId().toString(), 7);

    try {
      Barcode barcode = BarcodeFactory.createCode128B(idPadding + deductionPadding);
      // TODO: 2022/03/23 File ...로컬에 저장하지 않고 Response 주는 방법
      File file = new File("/Users/sunba/nsk/successmode/tpay-server/src/main/resources/testPNG.png");
      BarcodeImageHandler.savePNG(barcode, file);

      InputStream in = getClass().getResourceAsStream("/testPNG.png");
      return IOUtils.toByteArray(in);
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
