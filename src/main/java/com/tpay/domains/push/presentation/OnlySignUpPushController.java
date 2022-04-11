package com.tpay.domains.push.presentation;

import com.tpay.domains.push.application.OnlySignUpPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OnlySignUpPushController {

    private final OnlySignUpPushService onlySignUpPushService;

    @GetMapping("/fcm/one")
    public void onlySignUpPushService() {
        onlySignUpPushService.requestPushNotification();
    }
}
