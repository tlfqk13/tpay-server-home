package com.tpay.domains.notice.presentation;

import com.tpay.domains.notice.application.NoticeTestService;
import com.tpay.domains.notice.application.dto.AppNoticeFindDto;
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
@RequestMapping("/test")
public class NoticeTestController {

    private final NoticeTestService noticeTestService;

    @PostMapping("/admin/notice")
    public void registration(
        @RequestParam("dataList") String dataListString,
        @RequestParam(required = false) MultipartFile mainImg,
        @RequestParam(required = false) MultipartFile subImg1,
        @RequestParam(required = false) MultipartFile subImg2,
        @RequestParam(required = false) MultipartFile subImg3
    ) {
        noticeTestService.registration(dataListString, mainImg, subImg1, subImg2, subImg3);
    }

    @GetMapping("/admin/notice")
    public ResponseEntity<List<CommonNoticeFindDto.FindAllResponse>> getAll(
    ) {
        List<CommonNoticeFindDto.FindAllResponse> all = noticeTestService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/admin/notice/{noticeIndex}")
    public ResponseEntity<CommonNoticeFindDto.FindOneResponse> getOne(
        @PathVariable Long noticeIndex
    ) {
        CommonNoticeFindDto.FindOneResponse one = noticeTestService.getOne(noticeIndex);
        return ResponseEntity.ok(one);
    }

    @PatchMapping("/admin/notice/{noticeIndex}")
    public void updateInvisible(@PathVariable Long noticeIndex,
    @RequestBody InvisibleUpdateDto invisibleUpdateDto){
        noticeTestService.updateInvisible(noticeIndex,invisibleUpdateDto);
    }

    @DeleteMapping("/admin/notice/{noticeIndex}")
    public void deleteNotification(@PathVariable Long noticeIndex){
        noticeTestService.deleteNotification(noticeIndex);
    }

    @PutMapping("/admin/notice/{noticeIndex}")
    public void updateNotice(@PathVariable Long noticeIndex,
    @RequestBody DataList dataList ) {
        noticeTestService.updateNotice(noticeIndex,dataList);
    }

    @GetMapping("/notice")
    public ResponseEntity<AppNoticeFindDto.FindAllResponse> getAllApp(){
        AppNoticeFindDto.FindAllResponse result = noticeTestService.getAllApp();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/notice/{noticeIndex}")
    public ResponseEntity<CommonNoticeFindDto.FindOneResponse> getOneApp(@PathVariable Long noticeIndex){
        CommonNoticeFindDto.FindOneResponse oneApp = noticeTestService.getOneApp(noticeIndex);
        return ResponseEntity.ok(oneApp);
    }
}
