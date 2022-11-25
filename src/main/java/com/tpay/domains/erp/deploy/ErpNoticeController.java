package com.tpay.domains.erp.deploy;

import com.tpay.domains.notice.application.NoticeService;
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
@RequestMapping("/admin/notice")
public class ErpNoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public void registration(
            @RequestParam("dataList") String dataListString,
            @RequestParam(required = false) MultipartFile mainImg,
            @RequestParam(required = false) MultipartFile subImg1,
            @RequestParam(required = false) MultipartFile subImg2,
            @RequestParam(required = false) MultipartFile subImg3
    ) {
        noticeService.registration(dataListString, mainImg, subImg1, subImg2, subImg3);
    }

    @GetMapping
    public ResponseEntity<List<CommonNoticeFindDto.FindAllResponse>> getAll(
    ) {
        List<CommonNoticeFindDto.FindAllResponse> all = noticeService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{noticeIndex}")
    public ResponseEntity<CommonNoticeFindDto.FindOneResponse> getOne(
            @PathVariable Long noticeIndex
    ) {
        CommonNoticeFindDto.FindOneResponse one = noticeService.getOne(noticeIndex);
        return ResponseEntity.ok(one);
    }

    @PatchMapping("/{noticeIndex}")
    public void updateInvisible(@PathVariable Long noticeIndex,
                                @RequestBody InvisibleUpdateDto invisibleUpdateDto) {
        noticeService.updateInvisible(noticeIndex, invisibleUpdateDto);
    }

    @DeleteMapping("/{noticeIndex}")
    public void deleteNotification(@PathVariable Long noticeIndex) {
        noticeService.deleteNotification(noticeIndex);
    }

    @PutMapping("/{noticeIndex}")
    public void updateNotice(@PathVariable Long noticeIndex,
                             @RequestBody DataList dataList) {
        noticeService.updateNotice(noticeIndex, dataList);
    }
}
