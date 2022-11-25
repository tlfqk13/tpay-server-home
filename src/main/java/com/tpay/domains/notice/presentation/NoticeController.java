package com.tpay.domains.notice.presentation;

import com.tpay.domains.notice.application.NoticeService;
import com.tpay.domains.notice.application.dto.AppNoticeFindDto;
import com.tpay.domains.notice.application.dto.CommonNoticeFindDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

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
