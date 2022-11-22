package com.tpay.domains.franchisee.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.franchisee.application.FranchiseeSettingService;
import com.tpay.domains.franchisee.application.FranchiseeUpdateService;
import com.tpay.domains.franchisee.application.MyPageFindService;
import com.tpay.domains.franchisee.application.dto.FranchiseeSettingDto;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateDtoRequest;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateDtoResponse;
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
        @RequestBody FranchiseeSettingDto.Request request,
        @KtpIndexInfo IndexInfo indexInfo
    ) {
        FranchiseeSettingDto.Response response = franchiseeSettingService.changeSoundOrVibration(indexInfo.getIndex(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * 팝업 설정 변경
     */
    @PatchMapping("/{franchiseeIndex}/popUp")
    public ResponseEntity<Boolean> updatePopUp(@PathVariable Long franchiseeIndex,
                                               @KtpIndexInfo IndexInfo indexInfo) {
        boolean result = franchiseeUpdateService.updatePopUp(indexInfo.getIndex());
        return ResponseEntity.ok(result);
    }

    /**
     * 마이페이지 조회
     */
    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<MyPageResponse> findMyPageInfo(
            @PathVariable Long franchiseeIndex,
            @KtpIndexInfo IndexInfo indexInfo) {
        MyPageResponse response = myPageFindService.findByFranchiseeIndex(indexInfo.getIndex());
        return ResponseEntity.ok(response);
    }

    /**
     * 가맹점 상세 정보 수정
     */
    @PatchMapping("/{franchiseeIndex}")
    public ResponseEntity<FranchiseeUpdateDtoResponse> updateFranchisee(
        @PathVariable Long franchiseeIndex,
        @RequestBody FranchiseeUpdateDtoRequest franchiseeUpdateDtoRequest,
        @KtpIndexInfo IndexInfo indexInfo
    )  {
        FranchiseeUpdateDtoResponse result = franchiseeUpdateService.updateFranchisee(indexInfo.getIndex(), franchiseeUpdateDtoRequest);
        return ResponseEntity.ok(result);
    }
}
