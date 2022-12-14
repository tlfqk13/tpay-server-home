package com.tpay.domains.push.presentation;

import com.tpay.domains.push.application.AdminPushService;
import com.tpay.domains.push.application.dto.AdminNotificationDto;
import com.tpay.domains.push.application.dto.PushFindDto;
import com.tpay.domains.push.application.dto.PushNoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/push")
public class AdminPushController {

    private final AdminPushService adminPushService;

    @PostMapping
    public ResponseEntity<AdminNotificationDto.Response> requestFcmPush(@RequestBody AdminNotificationDto.Request request) {
        AdminNotificationDto.Response response = adminPushService.sendMessageByAdmin(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PushFindDto.FindAllResponse> findAll(
    ) {
        PushFindDto.FindAllResponse response = adminPushService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pushIndex}")
    public ResponseEntity<PushFindDto.Response> findDetail(@PathVariable Long pushIndex) {
        PushFindDto.Response response = adminPushService.findDetail(pushIndex);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notice")
    public ResponseEntity<PushNoticeDto> findAllNotice(){
        PushNoticeDto pushNoticeDto = adminPushService.findAllNotice();
        return ResponseEntity.ok(pushNoticeDto);
    }
}