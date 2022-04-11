package com.tpay.domains.push.application;

import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfoInterface;
import com.tpay.domains.push.application.dto.NotificationDto;
import com.tpay.domains.push.domain.UserPushTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.tpay.commons.push.PushType.TOKEN;
import static com.tpay.commons.util.UserSelector.FRANCHISEE;
import static com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus.INIT;
import static com.tpay.commons.util.DateConverter.stringToLocalDate;
import static com.tpay.commons.push.PushCategoryType.CASE_ONE;


@RequiredArgsConstructor
@Service
public class OnlySignUpPushService {

    private final PushNotificationService pushNotificationService;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final UserPushTokenService userPushTokenService;


    @Scheduled(cron = "0 0 14 * * *")
    public void requestPushNotification() {

        List<FranchiseeApplicantInfoInterface> franchiseeApplicantInfoInterfaces =
                franchiseeApplicantFindService.filterFranchiseeStatus(INIT);


        for (FranchiseeApplicantInfoInterface franchiseeApplicantInfoInterface : franchiseeApplicantInfoInterfaces) {
            LocalDate createdDate = stringToLocalDate(franchiseeApplicantInfoInterface.getCreatedDate());
            LocalDate now = LocalDate.now();

            if (Period.between(createdDate, now).getYears() >= 1 ||
                    Period.between(createdDate, now).getMonths() >= 1 ||
                    Math.abs(Period.between(createdDate, now).getDays()) >= 14) {

                UserPushTokenEntity userPushTokenEntity =
                        userPushTokenService.findByUserIdAndUserType(franchiseeApplicantInfoInterface.getFranchiseeApplicantIndex().toString(), FRANCHISEE)
                                .orElseThrow();

                NotificationDto.Request request = NotificationDto.Request.builder()
                        .pushCategory(CASE_ONE.getPushCategory())
                        .link(CASE_ONE.getLink())
                        .title(franchiseeApplicantInfoInterface.getStoreName() + " " + CASE_ONE.getTitle())
                        .body(CASE_ONE.getBody())
                        .pushType(TOKEN)
                        .pushTypeValue(userPushTokenEntity.getUserToken())
                        .build();

                pushNotificationService.sendMessage(request);

            }
        }


    }


}
