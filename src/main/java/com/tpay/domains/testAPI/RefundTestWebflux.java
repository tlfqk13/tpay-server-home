package com.tpay.domains.testAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
public class RefundTestWebflux {
  private final WebClient.Builder builder;

  @PostMapping("/testtestktp")
  @Transactional
  public ResponseEntity<TestResponse> webfluxTest(@RequestBody TestRequest testRequest) {
    String[] b_no = new String[1];
    b_no[0] = testRequest.getTestRequestData();
    String uri = "localhost:20001/testtestrefund";
    WebClient webClient = builder.build();
    TestResponse testResponse =
        webClient
            .post()
            .uri(uri)
            .bodyValue(testRequest)
            .retrieve()
            .bodyToMono(TestResponse.class)
            .block();
    return ResponseEntity.ok(testResponse);
  }
}
