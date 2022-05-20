package com.tpay.domains.notice.presentation;

import com.tpay.domains.notice.application.NoticeService;
import com.tpay.domains.notice.application.dto.NoticeFindDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/admin/notice")
    public void registration(
        @RequestParam("dataList") String dataListString,
        @RequestParam(required = false) MultipartFile mainImg,
        @RequestParam(required = false) MultipartFile subImg1,
        @RequestParam(required = false) MultipartFile subImg2,
        @RequestParam(required = false) MultipartFile subImg3
    ) {
        noticeService.registration(dataListString, mainImg, subImg1, subImg2, subImg3);
    }

    @GetMapping("/admin/notice")
    public ResponseEntity<List<NoticeFindDto.FindAllResponse>> getAll() {
        List<NoticeFindDto.FindAllResponse> all = noticeService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/admin/notice/{noticeIndex}")
    public ResponseEntity<NoticeFindDto.FindOneResponse> getOne(
        @PathVariable Long noticeIndex
    ) {
        NoticeFindDto.FindOneResponse one = noticeService.getOne(noticeIndex);
        return ResponseEntity.ok(one);
    }

    @PatchMapping("/admin/notice/{noticeIndex}")
    public void updateInvisible(@PathVariable Long noticeIndex){
        noticeService.updateInvisible(noticeIndex);
    }

    @DeleteMapping("/admin/notice/{noticeIndex}")
    public void deleteNotification(@PathVariable Long noticeIndex){
        noticeService.deleteNotification(noticeIndex);
    }

}
