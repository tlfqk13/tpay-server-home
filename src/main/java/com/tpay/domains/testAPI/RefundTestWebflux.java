package com.tpay.domains.testAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RefundTestWebflux {
  private final WebClient.Builder builder;

  @PostMapping("/testtestktpmy")
  @Transactional
  public ResponseEntity<TestResponse> webfluxTest(@RequestBody TestRequest testRequest) {
    List<String> b_noList = new ArrayList<>();
    b_noList.add(testRequest.getTestRequestData());
    TestListRequest testListRequest = TestListRequest.builder().b_no(b_noList).build();
    String uri = "localhost:20001/testtestrefund";
    WebClient webClient = builder.build();
    TestResponse testResponse =
        webClient
            .post()
            .uri(uri)
            .bodyValue(testListRequest)
            .retrieve()
            .bodyToMono(TestResponse.class)
            .block();
    return ResponseEntity.ok(testResponse);
  }
}
