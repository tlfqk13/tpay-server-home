package com.tpay.domains.erp.test.controller;

import com.tpay.domains.erp.test.service.NoticeTestService;
import com.tpay.domains.notice.application.dto.CommonNoticeFindDto;
import com.tpay.domains.notice.application.dto.DataList;
import com.tpay.domains.notice.application.dto.InvisibleUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/admin/notice")
public class ErpNoticeTestController {

    private final NoticeTestService noticeTestService;

    @PostMapping
    public void registration(
        @RequestParam("dataList") String dataListString,
        @RequestParam(required = false) MultipartFile mainImg,
        @RequestParam(required = false) MultipartFile subImg1,
        @RequestParam(required = false) MultipartFile subImg2,
        @RequestParam(required = false) MultipartFile subImg3
    ) {
        noticeTestService.registration(dataListString, mainImg, subImg1, subImg2, subImg3);
    }

    @GetMapping
    public ResponseEntity<List<CommonNoticeFindDto.FindAllResponse>> getAll(
    ) {
        List<CommonNoticeFindDto.FindAllResponse> all = noticeTestService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{noticeIndex}")
    public ResponseEntity<CommonNoticeFindDto.FindOneResponse> getOne(
        @PathVariable Long noticeIndex
    ) {
        CommonNoticeFindDto.FindOneResponse one = noticeTestService.getOne(noticeIndex);
        return ResponseEntity.ok(one);
    }

    @PatchMapping("/{noticeIndex}")
    public void updateInvisible(@PathVariable Long noticeIndex,
    @RequestBody InvisibleUpdateDto invisibleUpdateDto){
        noticeTestService.updateInvisible(noticeIndex,invisibleUpdateDto);
    }

    @DeleteMapping("/{noticeIndex}")
    public void deleteNotification(@PathVariable Long noticeIndex){
        noticeTestService.deleteNotification(noticeIndex);
    }

    @PutMapping("/{noticeIndex}")
    public void updateNotice(@PathVariable Long noticeIndex,
    @RequestBody DataList dataList ) {
        noticeTestService.updateNotice(noticeIndex,dataList);
    }

}
