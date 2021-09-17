package com.tpay.domains.certifications.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.certifications.application.dto.CertificateResponse;
import com.tpay.domains.certifications.application.dto.CertificationInfo;
import com.tpay.domains.certifications.application.dto.TokenRequest;
import com.tpay.domains.certifications.application.dto.TokenResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SMSCertificateService {

  private final WebClient.Builder builder;

  @Transactional
  public CertificateResponse certificate(String imp_uid) {
    String accessToken = getAccessToken();
    CertificationInfo certificationInfo = getCertifications(accessToken, imp_uid);

    return CertificateResponse.builder()
        .code(certificationInfo.getCode())
        .name(certificationInfo.getResponse().getName())
        .phoneNumber(certificationInfo.getResponse().getPhone())
        .build();
  }

  private CertificationInfo getCertifications(String accessToken, String imp_uid) {
    WebClient webClient = builder.build();

    String uri = "https://api.iamport.kr/certifications/" + imp_uid;
    CertificationInfo certificationInfo =
        webClient
            .get()
            .uri(uri)
            .header("Authorization", accessToken)
            .retrieve()
            .bodyToMono(CertificationInfo.class)
            .block();

    return certificationInfo;
  }

  private String getAccessToken() {
    WebClient webClient = builder.build();
    TokenResponse tokenResponse =
        webClient
            .post()
            .uri("https://api.iamport.kr/users/getToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                TokenRequest.builder()
                    .imp_key(CustomValue.IAMPORT_API_KEY)
                    .imp_secret(CustomValue.IAMPORT_SECRET)
                    .build())
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();
    return tokenResponse.getResponse().getAccess_token();
  }
}
