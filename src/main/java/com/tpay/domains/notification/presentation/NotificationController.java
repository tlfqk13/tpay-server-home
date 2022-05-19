package com.tpay.domains.notification.presentation;

import com.tpay.domains.notification.application.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/admin/notification")
    public void registration(
        @RequestParam("dataList") String dataListString,
        @RequestParam(required = false) MultipartFile mainImg,
        @RequestParam(required = false) MultipartFile subImg1,
        @RequestParam(required = false) MultipartFile subImg2,
        @RequestParam(required = false) MultipartFile subImg3
    ) {
        notificationService.registration(dataListString, mainImg, subImg1, subImg2, subImg3);
    }
}
