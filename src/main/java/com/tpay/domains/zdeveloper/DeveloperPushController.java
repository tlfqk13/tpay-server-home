package com.tpay.domains.zdeveloper;

import com.tpay.commons.push.PushCategoryType;
import com.tpay.domains.push.application.NonBatchPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeveloperPushController {

    private final NonBatchPushService nonBatchPushService;

    /**
     * 개발자용 강제푸시 생성기
     */
    @PostMapping("/fcm/developer")
    public void developerPush(@RequestBody DeveloperPushDto.Request request) {

        String pushTypeCategory = request.getPushTypeCategory();

        PushCategoryType pushCategoryType = PushCategoryType.CASE_ONE;
        switch (pushTypeCategory) {
            case "1":
                pushCategoryType = PushCategoryType.CASE_ONE;
                break;
            case "2":
                pushCategoryType = PushCategoryType.CASE_TWO;
                break;
            case "3":
                pushCategoryType = PushCategoryType.CASE_THREE;
                break;
            case "4":
                pushCategoryType = PushCategoryType.CASE_FOUR;
                break;
            case "5":
                pushCategoryType = PushCategoryType.CASE_FIVE;
                break;
            case "6":
                pushCategoryType = PushCategoryType.CASE_SIX;
                break;
            case "7":
                pushCategoryType = PushCategoryType.CASE_SEVEN;
                break;
            case "8":
                pushCategoryType = PushCategoryType.CASE_EIGHT;
                break;
            case "9":
                pushCategoryType = PushCategoryType.CASE_NINE;
                break;
            case "10":
                pushCategoryType = PushCategoryType.CASE_TEN;
                break;
            case "11":
                pushCategoryType = PushCategoryType.CASE_ELEVEN;
                break;
            case "12":
                pushCategoryType = PushCategoryType.CASE_TWELVE;
                break;
            case "13":
                pushCategoryType = PushCategoryType.CASE_THIRTEEN;
                break;
            case "14":
                pushCategoryType = PushCategoryType.CASE_FOURTEEN;
                break;
            case "15":
                pushCategoryType = PushCategoryType.CASE_FIFTEEN;
                break;
        }

        nonBatchPushService.nonBatchPushNSave(pushCategoryType, request.getFranchiseeIndex());

    }

    @PostMapping("/fcm/dev/push/refresh/{id}")
    public void refundRefreshPush(@PathVariable Long id) {
        nonBatchPushService.nonBatchPushNSave(PushCategoryType.REFUND_CANCEL_SCREEN_REFRESH_WITH_EXTERNAL, id, false);

    }

}
