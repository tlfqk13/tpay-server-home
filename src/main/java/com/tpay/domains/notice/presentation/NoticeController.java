package com.tpay.domains.notice.presentation;

import com.tpay.domains.notice.application.NoticeService;
import com.tpay.domains.notice.application.dto.CommonNoticeFindDto;
import com.tpay.domains.notice.application.dto.AppNoticeFindDto;
import com.tpay.domains.notice.application.dto.DataList;
import com.tpay.domains.notice.application.dto.InvisibleUpdateDto;
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

    @GetMapping("/admin/notice/list/{page}")
    public ResponseEntity<List<CommonNoticeFindDto.FindAllResponse>> getAll(
            @PathVariable int page
    ) {
        List<CommonNoticeFindDto.FindAllResponse> all = noticeService.getAll(page);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/admin/notice/{noticeIndex}")
    public ResponseEntity<CommonNoticeFindDto.FindOneResponse> getOne(
        @PathVariable Long noticeIndex
    ) {
        CommonNoticeFindDto.FindOneResponse one = noticeService.getOne(noticeIndex);
        return ResponseEntity.ok(one);
    }

    @PatchMapping("/admin/notice/{noticeIndex}")
    public void updateInvisible(@PathVariable Long noticeIndex,
    @RequestBody InvisibleUpdateDto invisibleUpdateDto){
        noticeService.updateInvisible(noticeIndex,invisibleUpdateDto);
    }

    @DeleteMapping("/admin/notice/{noticeIndex}")
    public void deleteNotification(@PathVariable Long noticeIndex){
        noticeService.deleteNotification(noticeIndex);
    }

    @PutMapping("/admin/notice/{noticeIndex}")
    public void updateNotice(@PathVariable Long noticeIndex,
    @RequestBody DataList dataList ) {
        noticeService.updateNotice(noticeIndex,dataList);
    }

    @GetMapping("/notice")
    public ResponseEntity<AppNoticeFindDto.FindAllResponse> getAllApp(){
        AppNoticeFindDto.FindAllResponse result = noticeService.getAllApp();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/notice/{noticeIndex}")
    public ResponseEntity<CommonNoticeFindDto.FindOneResponse> getOneApp(@PathVariable Long noticeIndex){
        CommonNoticeFindDto.FindOneResponse oneApp = noticeService.getOneApp(noticeIndex);
        return ResponseEntity.ok(oneApp);
    }
}
