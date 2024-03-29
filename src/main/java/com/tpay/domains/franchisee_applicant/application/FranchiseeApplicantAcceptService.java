package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeFindRequest;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.push.application.NonBatchPushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;

import static com.tpay.commons.push.PushCategoryType.CASE_TWO;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchiseeApplicantAcceptService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final FranchiseeBankFindService franchiseeBankFindService;
    private final WebClient.Builder builder;
    private final NonBatchPushService nonBatchPushService;

    @Transactional
    public FranchiseeFindRequest accept(Long franchiseeApplicantIndex, FranchiseeFindRequest franchiseeFindRequest) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);

        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
        franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
        FranchiseeFindRequest response = sendTo(franchiseeFindRequest);
        franchiseeApplicantEntity.accept();

        franchiseeEntity.memberInfo(response.getFranchiseeName(), response.getFranchiseeNumber());

        //푸시 전송
        nonBatchPushService.nonBatchPushNSave(CASE_TWO, franchiseeEntity.getId());

        // TODO: 2022/06/15 필수입력으로 전환시 if 제거
        try {
            if(franchiseeFindRequest.getBalancePercentage() != 0){
                double balancePercentage = franchiseeFindRequest.getBalancePercentage();
                franchiseeEntity.updateBalancePercentage(balancePercentage);
            }
        } catch (NullPointerException e){
            log.trace("franchiseeApplicantIndex : {} Accepted", franchiseeApplicantIndex);
        }

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
