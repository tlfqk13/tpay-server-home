package com.tpay.domains.auth.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.auth.application.AccountDeleteService;
import com.tpay.domains.auth.application.SignInService;
import com.tpay.domains.auth.application.SignOutService;
import com.tpay.domains.auth.application.TokenUpdateService;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import com.tpay.domains.auth.application.dto.TokenRefreshVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth 관련
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final SignOutService signOutService;
    private final TokenUpdateService tokenUpdateService;
    private final SignInService signInService;

    private final AccountDeleteService accountDeleteService;

    /**
     * 로그인
     */
    @PostMapping("/sign-in")
    public ResponseEntity<SignInTokenInfo> signIn(@RequestBody SignInRequest signInRequest) {
        SignInTokenInfo signInTokenInfo = signInService.signIn(signInRequest);
        return ResponseEntity.ok(signInTokenInfo);
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/sign-out")
    public ResponseEntity<String> signOut(@KtpIndexInfo IndexInfo indexInfo) {
        signOutService.signOut(indexInfo);
        return ResponseEntity.ok("logout");
    }

    /**
     * 중복 로그아웃
     * Sign-in 에서 이미 중복인 경우 프론트에 전달하게됨
     * 어떤 프렌차이즈나 직원이 강제 로그아웃 되었다는 로그를 남기기위해 존재하는듯
     */
    @DeleteMapping("/duplicate-sign-out")
    public ResponseEntity<String> duplicateSignOut(@RequestBody SignOutRequest signOutRequest) {
        String result = signOutService.duplicateSignOut(signOutRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 엑세스토큰 갱신
     */
    @PatchMapping("/refresh")
    public ResponseEntity<SignInTokenInfo> refresh(
            @RequestBody TokenRefreshVo.Request tokenRefreshInfo) {
        return ResponseEntity.ok(tokenUpdateService.refresh(tokenRefreshInfo));
    }

    //TODO: URI 교체 작업시 전달 DTO 변경해서 작업 예정
//    @PatchMapping("/refresh")
//    public ResponseEntity<TokenRefreshVo.Response> refresh(
//            @RequestBody TokenRefreshVo.Request signInTokenInfo) {
//        return ResponseEntity.ok(tokenUpdateService.refreshNew(signInTokenInfo));
//    }

    /**
     * 계정 삭제
     * 심사를 위해 넣은 기능이고 실제 사용되진 않는다
     */
    @DeleteMapping("/delete-account")
    public ResponseEntity<String> accountDelete(
            @KtpIndexInfo IndexInfo indexInfo
    ){
        String result = accountDeleteService.deleteAccount(indexInfo);
        return ResponseEntity.ok(result);
    }
}
