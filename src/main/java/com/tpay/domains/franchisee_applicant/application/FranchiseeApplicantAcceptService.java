package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantAcceptService {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;
  private final WebClient.Builder builder;

  @Transactional
  public FranchiseeInfo accept(Long franchiseeApplicantIndex, FranchiseeInfo franchiseeInfo) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);

    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

    FranchiseeInfo response = sendTo(franchiseeInfo);

    franchiseeApplicantEntity.accept();
    franchiseeEntity.memberInfo(response.getFranchiseeName(), response.getFranchiseeNumber());
    return response;
  }

  private FranchiseeInfo sendTo(FranchiseeInfo franchiseeInfo) {
    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/franchisee";

    FranchiseeInfo response =
        webClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(franchiseeInfo)
            .retrieve()
            .bodyToMono(FranchiseeInfo.class)
            .block();

    return response;
  }
}
