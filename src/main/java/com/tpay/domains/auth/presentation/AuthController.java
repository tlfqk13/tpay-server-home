package com.tpay.domains.auth.presentation;

import com.tpay.domains.auth.application.SignInService;
import com.tpay.domains.auth.application.SignOutService;
import com.tpay.domains.auth.application.TokenUpdateService;
import com.tpay.domains.auth.application.dto.SignInRequest;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.auth.application.dto.SignOutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Auth 관련
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final SignOutService signOutService;
    private final TokenUpdateService tokenUpdateService;
    private final SignInService signInService;

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
    public ResponseEntity<String> signOut(@RequestBody SignOutRequest signOutRequest) {
        String result = signOutService.signOut(signOutRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 중복 로그아웃
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
        @RequestBody SignInTokenInfo signInTokenInfo) {
        return ResponseEntity.ok(tokenUpdateService.refresh(signInTokenInfo));
    }
}
