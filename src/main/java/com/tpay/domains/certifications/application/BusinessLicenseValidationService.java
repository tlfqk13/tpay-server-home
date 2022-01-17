package com.tpay.domains.certifications.application;


import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.certifications.application.dto.BusinessValidRequest;
import com.tpay.domains.certifications.application.dto.BusinessValidRequestList;
import com.tpay.domains.certifications.application.dto.BusinessValidResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessLicenseValidationService {

  public BusinessValidResponse valid(BusinessValidRequest businessValidRequest) {
    List<String> businessNumberList = new ArrayList<>();
    businessNumberList.add(businessValidRequest.getBusinessNumber());
    BusinessValidRequestList businessValidRequestList = BusinessValidRequestList.builder().b_no(businessNumberList).build();
    WebClient webClient = WebClient.builder().build();
    JSONObject jsonObject = new JSONObject("{\"b_no\":[\"2390401226\"]}");
    String s = JSONObject.valueToString(businessValidRequestList);

//    String baseUri = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=";
//    String uri = baseUri + CustomValue.BUSINESS_VALID_ACCESS;
    String uri = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=7BNL%2FH3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC%2BOtWootlhEk5%2FOiDAvXAcNGneemJ%2FtImQBpORzcEG0IPhQ%3D%3D";
//    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=7BNL/H3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC+OtWootlhEk5/OiDAvXAcNGneemJ/tImQBpORzcEG0IPhQ==");
//    URI endUri = builder.build().encode().toUri();
//    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri).queryParam("serviceKey","7BNL%2FH3LcgXHdOPmpxUXTMJRnYvv1BXLKAK76lLC%2BOtWootlhEk5%2FOiDAvXAcNGneemJ%2FtImQBpORzcEG0IPhQ%3D%3D");
//    URI endUri = builder.build().encode().toUri();
//    ResponseEntity<String> res = new RestTemplate().postForEntity(endUri,businessValidRequestList,String.class);
//    System.out.println(res.getBody());
    BusinessValidResponse businessValidResponse =
        webClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(s)
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
