package com.tpay.domains.push.test.presentation;

import com.tpay.domains.push.test.application.PushNotificationService;
import com.tpay.domains.push.test.application.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FcmPushApiController {

    private final PushNotificationService pushNotificationService;

    @PostMapping("/fcm/push")
    public void requestFcmPush(@RequestBody NotificationDto.Request request) {

        try {
            pushNotificationService.sendMessage(request);
        } catch (Exception e) {
            log.error(e.getMessage());

        }
    }


}