package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeFindRequest;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import javax.transaction.Transactional;

import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantAcceptService {

  private final FranchiseeApplicantFindService franchiseeApplicantFindService;
  private final FranchiseeBankFindService franchiseeBankFindService;
  private final WebClient.Builder builder;

  @Transactional
  public FranchiseeFindRequest accept(Long franchiseeApplicantIndex, FranchiseeFindRequest franchiseeFindRequest) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);


    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
    franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
    FranchiseeFindRequest response = sendTo(franchiseeFindRequest);
    franchiseeApplicantEntity.accept();
    franchiseeEntity.memberInfo(response.getFranchiseeName(), response.getFranchiseeNumber());
    return response;
  }

  private FranchiseeFindRequest sendTo(FranchiseeFindRequest franchiseeFindRequest) {
    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/franchisee";

    FranchiseeFindRequest response =
        webClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(franchiseeFindRequest)
            .retrieve()
            .bodyToMono(FranchiseeFindRequest.class)
            .block();

    return response;
  }
}
