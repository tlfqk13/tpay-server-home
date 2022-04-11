package com.tpay.domains.push.presentation;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.push.application.UserPushTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserPushTokenController {

    private final UserPushTokenService userPushTokenService;

    @GetMapping("/fcm/token/{userType}/{userId}")
    public String findTokenByUserIdAndUserType(@PathVariable UserSelector userType, @PathVariable String userId) {
        return userPushTokenService.findTokenByUserIdAndUserType(userId, userType);
    }
}
