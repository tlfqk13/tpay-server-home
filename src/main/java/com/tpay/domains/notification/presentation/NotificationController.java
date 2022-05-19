package com.tpay.domains.notification.presentation;

import com.tpay.domains.notification.application.NotificationService;
import com.tpay.domains.notification.application.dto.NotificationFindDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/admin/notification")
    public ResponseEntity<List<NotificationFindDto.FindAllResponse>> getAll() {
        List<NotificationFindDto.FindAllResponse> all = notificationService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/admin/notification/{notificationIndex}")
    public ResponseEntity<NotificationFindDto.FindOneResponse> getOne(
        @PathVariable Long notificationIndex
    ) {
        NotificationFindDto.FindOneResponse one = notificationService.getOne(notificationIndex);
        return ResponseEntity.ok(one);
    }

    @PatchMapping("/admin/notification/{notificationIndex}")
    public void updateInvisible(@PathVariable Long notificationIndex){
        notificationService.updateInvisible(notificationIndex);
    }

    @DeleteMapping("/admin/notification/{notificationIndex}")
    public void deleteNotification(@PathVariable Long notificationIndex){
        notificationService.deleteNotification(notificationIndex);
    }

}
