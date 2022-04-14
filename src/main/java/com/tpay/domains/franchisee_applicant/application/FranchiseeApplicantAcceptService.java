package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.push.detail.PushTopic;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeFindRequest;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.push.application.PushNotificationService;
import com.tpay.domains.push.application.TopicSubscribeService;
import com.tpay.domains.push.application.UserPushTokenService;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.List;

import static com.tpay.commons.push.PushCategoryType.CASE_TWO;
import static com.tpay.commons.push.PushType.TOKEN;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantAcceptService {

    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final FranchiseeBankFindService franchiseeBankFindService;
    private final WebClient.Builder builder;

    private final TopicSubscribeService topicSubscribeService;
    private final UserPushTokenService userPushTokenService;
    private final PushNotificationService pushNotificationService;

    @Transactional
    public FranchiseeFindRequest accept(Long franchiseeApplicantIndex, FranchiseeFindRequest franchiseeFindRequest) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
            franchiseeApplicantFindService.findByIndex(franchiseeApplicantIndex);

        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
        franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
        FranchiseeFindRequest response = sendTo(franchiseeFindRequest);
        franchiseeApplicantEntity.accept();

        franchiseeEntity.memberInfo(response.getFranchiseeName(), response.getFranchiseeNumber());

        //토픽 구독
        UserPushTokenEntity userPushTokenEntity = userPushTokenService.findByFranchiseeIndex(franchiseeEntity.getId());
        List<String> tokenList = List.of(userPushTokenEntity.getUserToken());
        topicSubscribeService.subscribe(tokenList, PushTopic.FRANCHISEE);

        //푸시 전송
        NotificationDto.Request request = new NotificationDto.Request(CASE_TWO, TOKEN, userPushTokenEntity.getUserToken());
        pushNotificationService.sendMessageByToken(request);

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
