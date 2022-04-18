package com.tpay.domains.push.presentation;

import com.tpay.domains.push.application.AdminPushService;
import com.tpay.domains.push.application.dto.AdminNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminPushController {

    private final AdminPushService adminPushService;

    @PostMapping("/admin/push")
    public ResponseEntity<AdminNotificationDto.Response> requestFcmPush(@RequestBody AdminNotificationDto.Request request) {
        AdminNotificationDto.Response response = adminPushService.sendMessageByAdmin(request);
        return ResponseEntity.ok(response);
    }
}