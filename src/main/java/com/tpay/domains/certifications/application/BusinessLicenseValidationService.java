package com.tpay.domains.certifications.application;


import com.tpay.domains.certifications.application.dto.BusinessValidApiRequest;
import com.tpay.domains.certifications.application.dto.BusinessValidRequest;
import com.tpay.domains.certifications.application.dto.BusinessValidResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BusinessLicenseValidationService {

  public BusinessValidResponse valid(BusinessValidRequest businessValidRequest) {
    String[] businessValidRequestArray = new String[1];
    businessValidRequestArray[0] = businessValidRequest.getBusinessNumber();
    BusinessValidApiRequest businessValidApiRequest = BusinessValidApiRequest.builder().b_no(businessValidRequestArray).build();
    System.out.println(businessValidApiRequest.getB_no()[0]);
    WebClient webClient = WebClient.builder().build();
//    String baseUri = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=";
//    String uri = baseUri+CustomValue.BUSINESS_VALID_ACCESS;
    String uri = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=7BNL%2FH3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC%2BOtWootlhEk5%2FOiDAvXAcNGneemJ%2FtImQBpORzcEG0IPhQ%3D%3D";
    int a = 1;
    BusinessValidResponse businessValidResponse =
        webClient
            .post()
            .uri(uri)
            .bodyValue(businessValidApiRequest)
            .retrieve()
//            .onStatus(
//                HttpStatus::isError,
//                clientResponse -> clientResponse.bodyToMono(ExceptionResponse.class).flatMap(error -> Mono.error(new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Exception at WebFlux")))
//            )
            .bodyToMono(BusinessValidResponse.class)
            .block();
    return businessValidResponse;
  }
}
