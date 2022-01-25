package com.tpay.domains.certifications.application;


import com.tpay.domains.certifications.application.dto.BusinessValidRequest;
import com.tpay.domains.certifications.application.dto.BusinessValidRequestList;
import com.tpay.domains.certifications.application.dto.BusinessValidResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessLicenseValidationService {

  public BusinessValidResponse valid(BusinessValidRequest businessValidRequest) {
    List<String> businessNumberList = new ArrayList<>();
    businessNumberList.add(businessValidRequest.getBusinessNumber());
    BusinessValidRequestList businessValidRequestList = BusinessValidRequestList.builder().b_no(businessNumberList).build();
    WebClient webClient = WebClient.builder().baseUrl("https://api.odcloud.kr/api/nts-businessman/v1/status").build();
//    JSONObject jsonObject = new JSONObject("{\"b_no\":[\"2390401226\"]}");
//    String s = JSONObject.valueToString(businessValidRequestList);
//    String uri = baseUri + CustomValue.BUSINESS_VALID_ACCESS;
//    String uri = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=7BNL%2FH3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC%2BOtWootlhEk5%2FOiDAvXAcNGneemJ%2FtImQBpORzcEG0IPhQ%3D%3D";
//    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=7BNL/H3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC+OtWootlhEk5/OiDAvXAcNGneemJ/tImQBpORzcEG0IPhQ==");
//    URI endUri = builder.build().encode().toUri();
//    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri).queryParam("serviceKey","7BNL%2FH3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC%2BOtWootlhEk5%2FOiDAvXAcNGneemJ%2FtImQBpORzcEG0IPhQ%3D%3D");
//    URI endUri = builder.build().encode().toUri();
//    ResponseEntity<String> res = new RestTemplate().postForEntity(endUri,businessValidRequestList,String.class);
//    System.out.println(res.getBody());
    int a = 1;
    BusinessValidResponse businessValidResponse =
        webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .queryParam("serviceKey", "7BNL%2FH3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC%2BOtWootlhEk5%2FOiDAvXAcNGneemJ%2FtImQBpORzcEG0IPhQ%3D%3D")
                .build())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(businessValidRequest)
            .retrieve()
//            .onStatus(
//                HttpStatus::isError,
//                clientResponse -> clientResponse.bodyToMono(ExceptionResponse.class).flatMap(error -> Mono.error(new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Exception at WebFlux")))
//            )
            .bodyToMono(BusinessValidResponse.class)
            .block();
    return BusinessValidResponse.builder().request_cnt("1").build();
//    return businessValidResponse;
  }
}
