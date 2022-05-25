package com.tpay.domains.franchisee.presentation;

import com.tpay.domains.franchisee.application.FranchiseeSettingService;
import com.tpay.domains.franchisee.application.FranchiseeUpdateService;
import com.tpay.domains.franchisee.application.MyPageFindService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSettingDto;
import com.tpay.domains.franchisee.application.dto.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 앱 내 가맹점 기능
 */
@RestController
@RequestMapping("/franchisee")
@RequiredArgsConstructor
public class FranchiseeController {

    private final FranchiseeSettingService franchiseeSettingService;
    private final FranchiseeUpdateService franchiseeUpdateService;
    private final MyPageFindService myPageFindService;



    /**
     * 세팅 변경
     */
    @PatchMapping("/{franchiseeIndex}/settings")
    public ResponseEntity<FranchiseeSettingDto.Response> changeSoundOrVibration(
        @PathVariable Long franchiseeIndex,
        @RequestBody FranchiseeSettingDto.Request request
    ) {
        FranchiseeSettingDto.Response response = franchiseeSettingService.changeSoundOrVibration(franchiseeIndex, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 팝업 설정 변경
     */
    @PatchMapping("/{franchiseeIndex}/popUp")
    public ResponseEntity<Boolean> updatePopUp(@PathVariable Long franchiseeIndex) {
        boolean result = franchiseeUpdateService.updatePopUp(franchiseeIndex);
        return ResponseEntity.ok(result);
    }

    /**
     * 마이페이지 조회
     */
    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<MyPageResponse> findMyPageInfo(
        @PathVariable Long franchiseeIndex) {
        MyPageResponse response = myPageFindService.findByFranchiseeIndex(franchiseeIndex);
        return ResponseEntity.ok(response);
    }
}
