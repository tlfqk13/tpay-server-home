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
public class AdminPushController {

    private final AdminPushService adminPushService;

    @PostMapping("/admin/push")
    public ResponseEntity<AdminNotificationDto.Response> requestFcmPush(@RequestBody AdminNotificationDto.Request request) {
        AdminNotificationDto.Response response = adminPushService.sendMessageByAdmin(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/push/list/{page}")
    public ResponseEntity<PushFindDto.FindAllResponse> findAll(
            @PathVariable int page
    ) {
        PushFindDto.FindAllResponse response = adminPushService.findAll(page);
        return ResponseEntity.ok(response);
    }

    // TODO: 2022/07/22 page를 requestParam으로 받아보자
    @GetMapping("/admin/push")
    public ResponseEntity<PushFindDto.FindAllResponse> findAllReqParam(
            @RequestParam int page
    ) {
        PushFindDto.FindAllResponse response = adminPushService.findAll(page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/push/{pushIndex}")
    public ResponseEntity<PushFindDto.Response> findDetail(@PathVariable Long pushIndex) {
        PushFindDto.Response response = adminPushService.findDetail(pushIndex);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/push/notice")
    public ResponseEntity<PushNoticeDto> findAllNotice(){
        PushNoticeDto pushNoticeDto = adminPushService.findAllNotice();
        return ResponseEntity.ok(pushNoticeDto);
    }
}